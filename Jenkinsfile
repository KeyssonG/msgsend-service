pipeline {
    agent any

    options {
        disableConcurrentBuilds()
    }

    environment {
        DOCKERHUB_IMAGE = "keyssong/msgsend"
        IMAGE_TAG = "latest"
        DEPLOYMENT_FILE = "k8s/msgsend-deployment.yaml"
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

        stage('Build da Imagem Docker') {
            steps {
                sh """
                    docker build -t ${DOCKERHUB_IMAGE}:${IMAGE_TAG} .
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
                    sh """
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${DOCKERHUB_IMAGE}:${IMAGE_TAG}
                    """
                }
            }
        }

        stage('Atualizar deployment.yaml (GitOps)') {
            steps {
                sh """
                    git config user.email "jenkins@pipeline.com"
                    git config user.name "Jenkins"

                    sed -i 's|image: .*|image: ${DOCKERHUB_IMAGE}:${IMAGE_TAG}|' ${DEPLOYMENT_FILE}

                    git add ${DEPLOYMENT_FILE}

                    if ! git diff --cached --quiet; then
                        git commit -m "Atualiza imagem Docker"
                        git push origin master
                        echo "Deployment atualizado no Git"
                    else
                        echo "Nenhuma alteração detectada"
                    fi
                """
            }
        }
    }

    post {
        success {
            echo "✅ Pipeline executada com sucesso"
        }
        failure {
            echo "❌ Pipeline falhou"
        }
    }
}
