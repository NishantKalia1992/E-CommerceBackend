pipeline{
	    agent any

    tools {
        maven 'Maven_3_9_9'
        jdk 'jdk-21'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build JAR') {
            steps {
                echo 'Building the JAR file...'
                echo 'Using Maven to clean and package the application...'
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Buils and push Docker Image') {
            steps {
                echo 'Deploying the application using Docker Compose...'
                withCredentials([usernamePassword(credentialsId: 'order-credential-Id', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                   bat "docker login -u %DOCKERHUB_USERNAME% -p %DOCKERHUB_PASSWORD%"
                   bat "docker build -t ${DOCKERHUB_USERNAME}/order-service:latest ."
                   bat "docker push ${DOCKERHUB_USERNAME}/order-service:latest"
                }
            }
        }
        stage('Deploy to Docker Desktop') {
            steps {
                echo 'Deploying application and database locally...'
                bat 'docker-compose down'
                bat 'docker-compose up -d'
            }
        }
    }
}