pipeline {
    agent any

    options {
        // Evita checkout automático duplicado do Declarative Pipeline
        skipDefaultCheckout(true)
        disableConcurrentBuilds()
    }

    environment {
        DOCKERHUB_IMAGE = "keyssong/msgsend"
        IMAGE_TAG = "latest"
        DEPLOYMENT_FILE = "k8s/msgsend-deployment.yaml"
    }

    stages {

        stage('Checkout SCM') {
            steps {
                // Checkout controlado (repo completo com .git)
                checkout scm
            }
        }

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
                    docker tag ${DOCKERHUB_IMAGE}:${IMAGE_TAG} ${DOCKERHUB_IMAGE}:latest
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
                        docker push ${DOCKERHUB_IMAGE}:latest
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
                        git commit -m "Atualiza imagem Docker para ${IMAGE_TAG}"
                        git push origin master
                        echo "Deployment atualizado e enviado para o Git"
                    else
                        echo "Nenhuma alteração detectada no deployment"
                    fi
                """
            }
        }
    }

    post {
        success {
            echo "✅ Pipeline concluída com sucesso! ArgoCD aplicará as mudanças automaticamente 🚀"
        }
        failure {
            echo "❌ Pipeline falhou. Verifique os logs acima."
        }
    }
}
