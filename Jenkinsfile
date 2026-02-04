pipeline {
    agent any

    environment {
        DOCKER_REPO = 'gokuwnl'   
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/Sourabh00465/Anime-FULLstack.git'
            }
        }

        stage('Build Backend') {
            steps {
                dir('hokage') {
                    bat 'mvn clean package -DskipTests'
                    bat "docker build -t ${DOCKER_REPO}/anime-backend:latest ."
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('anime-ui') {
                    bat 'npm install'
                    bat 'npm run build'
                    bat "docker build -t ${DOCKER_REPO}/anime-frontend:latest ."
                }
            }
        }

        stage('Push Images') {
            steps {
                script {
                    withDockerRegistry([credentialsId: 'dockerhub-creds', url: 'https://index.docker.io/v1/']) {
                        bat "docker push ${DOCKER_REPO}/anime-backend:latest"
                        bat "docker push ${DOCKER_REPO}/anime-frontend:latest"
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                bat 'docker-compose down --remove-orphans' 
                bat 'docker rm -f anime-frontend || true' 
                bat 'docker rm -f hokage-mysql || true'
                bat 'docker-compose up -d --force-recreate'
            }
        }
    }
}
