pipeline {
    agent any

    environment {
        DOCKER_REPO = 'GokuWNL'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Sourabh00465/Anime-Cards.git'
            }
        }

        stage('Build Backend') {
            steps {
                dir('hokage') {
                    bat 'mvn clean package -DskipTests'
                    bat "docker build -t %DOCKER_REPO%/anime-backend:latest ."
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('anime-frontend') {
                    bat 'npm install'
                    bat 'npm run build'
                    bat "docker build -t %DOCKER_REPO%/anime-frontend:latest ."
                }
            }
        }

        stage('Push Images') {
            steps {
                withDockerRegistry([credentialsId: 'dockerhub-creds', url: '']) {
                    bat "docker push %DOCKER_REPO%/anime-backend:latest"
                    bat "docker push %DOCKER_REPO%/anime-frontend:latest"
                }
            }
        }

        stage('Deploy') {
            steps {
                bat 'docker-compose down'
                bat 'docker-compose up -d'
            }
        }
    }
}
