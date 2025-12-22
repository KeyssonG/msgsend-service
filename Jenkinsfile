pipeline {
    agent any

    environment {
        DOCKERHUB_IMAGE = "keyssong/msgsend"
        DEPLOYMENT_FILE = "k8s/msgsend-deployment.yaml"
        IMAGE_TAG = "latest"
        WSL_DISTRO = "Ubuntu"
    }

    triggers {
        pollSCM('* * * * *')
    }

    options {
        disableConcurrentBuilds()
    }

    stages {

        stage('Verificar Branch') {
            when {
                branch 'master'
            }
            steps {
                echo "Executando pipeline na branch master"
            }
        }

        stage('Checkout do Código') {
            steps {
                git credentialsId: 'github',
                    url: 'https://github.com/KeyssonG/api-msgsend.git',
                    branch: 'master'
            }
        }

        stage('Build da Imagem Docker') {
            steps {
                bat """
                wsl -d %WSL_DISTRO% -- bash -lc "docker build -t ${DOCKERHUB_IMAGE}:${IMAGE_TAG} ."
                wsl -d %WSL_DISTRO% -- bash -lc "docker tag ${DOCKERHUB_IMAGE}:${IMAGE_TAG} ${DOCKERHUB_IMAGE}:latest"
                """
            }
        }

        stage('Push da Imagem para Docker Hub') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    bat """
                    wsl -d %WSL_DISTRO% -- bash -lc "echo '%DOCKER_PASS%' | docker login -u '%DOCKER_USER%' --password-stdin"
                    wsl -d %WSL_DISTRO% -- bash -lc "docker push ${DOCKERHUB_IMAGE}:${IMAGE_TAG}"
                    wsl -d %WSL_DISTRO% -- bash -lc "docker push ${DOCKERHUB_IMAGE}:latest"
                    """
                }
            }
        }

        stage('Atualizar deployment.yaml') {
            steps {
                bat """
                wsl -d %WSL_DISTRO% -- bash -lc "sed -i 's|image:.*|image: ${DOCKERHUB_IMAGE}:${IMAGE_TAG}|g' ${DEPLOYMENT_FILE}"

                wsl -d %WSL_DISTRO% -- bash -lc "git config user.email 'jenkins@pipeline.com'"
                wsl -d %WSL_DISTRO% -- bash -lc "git config user.name 'Jenkins'"
                wsl -d %WSL_DISTRO% -- bash -lc "git add ${DEPLOYMENT_FILE}"
                wsl -d %WSL_DISTRO% -- bash -lc \"git diff --cached --quiet || git commit -m 'Atualiza imagem Docker para latest'\"
                """
            }
        }
    }

    post {
        success {
            echo "Pipeline concluída com sucesso! A imagem '${DOCKERHUB_IMAGE}:latest' foi atualizada e o ArgoCD aplicará as alterações automaticamente. 🚀"
        }
        failure {
            echo "Erro na pipeline. Confira os logs para mais detalhes."
        }
    }
}
