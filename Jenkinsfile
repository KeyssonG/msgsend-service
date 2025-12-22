pipeline {
    agent any

    environment {
        DOCKERHUB_IMAGE = "keyssong/msgsend"
        DEPLOYMENT_FILE = "k8s/msgsend-deployment.yaml"
        IMAGE_TAG = "latest"
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

        stage('Atualizar deployment.yaml') {
            steps {
                sh """
                    git config user.email "jenkins@pipeline.com"
                    git config user.name "Jenkins"

                    sed -i 's|image: .*|image: ${DOCKERHUB_IMAGE}:${IMAGE_TAG}|' ${DEPLOYMENT_FILE}

                    git add ${DEPLOYMENT_FILE}

                    if ! git diff --cached --quiet; then
                      git commit -m "Atualiza imagem Docker para ${IMAGE_TAG}"
                      git push origin master
                    else
                      echo "Nenhuma alteração detectada no deployment."
                    fi
                """
            }
        }
    }
}
