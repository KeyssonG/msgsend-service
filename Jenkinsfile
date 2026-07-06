pipeline {
    agent any

    environment {
        DOCKERHUB_IMAGE = "keyssong/msgsend"
        IMAGE_TAG = "latest"
        DEPLOYMENT_FILE = "k8s/msgsend-deployment.yaml"
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
                checkout scm
            }
        }

        stage('Build da Imagem Docker') {
            steps {
                powershell script: '''
                    docker build -t $env:DOCKERHUB_IMAGE:$env:IMAGE_TAG .
                    docker tag $env:DOCKERHUB_IMAGE:$env:IMAGE_TAG $env:DOCKERHUB_IMAGE:latest
                '''
            }
        }

        stage('Push da Imagem para Docker Hub') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: '9dc53a7e-e45d-4c90-90aa-e499be366396',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    powershell script: '''
                        "$env:DOCKER_PASS" | docker login -u "$env:DOCKER_USER" --password-stdin
                        docker push $env:DOCKERHUB_IMAGE:$env:IMAGE_TAG
                        docker push $env:DOCKERHUB_IMAGE:latest
                    '''
                }
            }
        }

        stage('Atualizar deployment.yaml (GitOps)') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'be0b606d-4fdf-492f-a432-d091286311f4',
                        usernameVariable: 'GIT_USER',
                        passwordVariable: 'GIT_TOKEN'
                    )
                ]) {
                    powershell script: '''
                        git checkout master

                        git config user.email "jenkins@pipeline.com"
                        git config user.name "Jenkins"

                        git remote set-url origin https://$env:GIT_USER@$env:GIT_TOKEN@github.com/KeyssonG/msgsend-service.git

                        (Get-Content -Path $env:DEPLOYMENT_FILE) -replace 'image: .*', "image: $env:DOCKERHUB_IMAGE`:$env:IMAGE_TAG" | Set-Content -Path $env:DEPLOYMENT_FILE

                        git add $env:DEPLOYMENT_FILE

                        git diff --cached --quiet; if ($LASTEXITCODE -ne 0) {
                            git commit -m "Atualiza imagem Docker para latest"
                            git push origin master
                            echo "Alterações detectadas e enviadas ao repositório."
                        } else {
                            echo "Nenhuma alteração detectada no deployment.yaml"
                        }
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline concluída com sucesso! Imagem atualizada e GitOps acionado."
        }
        failure {
            echo "Erro na pipeline. Verifique os logs."
        }
    }
}
