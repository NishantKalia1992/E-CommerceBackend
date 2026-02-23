pipeline {
    agent any
    
    tools {
        maven 'Maven_3_9_9' 
        jdk 'jdk-21'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Downloading entire E-Commerce Backend from GitHub...'
                checkout scm
            }
        }
        
        stage('Build & Push Discovery Server') {
            steps {
                dir('DescoveryServer') { 
                    echo 'Building Discovery Server...'
                    bat 'mvn clean package -DskipTests'
                    withCredentials([usernamePassword(credentialsId: 'DescoveryServer-credential-Id', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                        bat "docker login -u %DOCKERHUB_USERNAME% -p %DOCKERHUB_PASSWORD%"
                        bat 'docker build -t %DOCKERHUB_USERNAME%/discovery-server:latest .'
                        bat 'docker push %DOCKERHUB_USERNAME%/discovery-server:latest'
                    }
                }
            }
        }
        
        stage('Build & Push Customer Service') {
            steps {
                dir('Customer') {
                    echo 'Building Customer Service...'
                    bat 'mvn clean package -DskipTests'
                    withCredentials([usernamePassword(credentialsId: 'customer-credential-Id', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                        bat 'docker build -t %DOCKERHUB_USERNAME%/customer-service:latest .'
                        bat 'docker push %DOCKERHUB_USERNAME%/customer-service:latest'
                    }
                }
            }
        }

        stage('Build & Push Order Service') {
            steps {
                dir('Order') {
                    echo 'Building Order Service...'
                    bat 'mvn clean package -DskipTests'
                    withCredentials([usernamePassword(credentialsId: 'order-credential-Id', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                        bat 'docker build -t %DOCKERHUB_USERNAME%/order-service:latest .'
                        bat 'docker push %DOCKERHUB_USERNAME%/order-service:latest'
                    }
                }
            }
        }
        
        stage('Deploy Ecosystem') {
            steps {
                echo 'Starting Discovery, Customer, Order, Kafka, and MySQL simultaneously...'
                bat 'docker-compose down'
                bat 'docker-compose up -d'
            }
        }
    }
}