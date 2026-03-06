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
        
        stage('Build & Push Service Registry') {
            steps {
                dir('ServiceRegistry') { 
                    echo 'Building Discovery Server...'
                    bat 'mvn clean package -DskipTests'
                    withCredentials([usernamePassword(credentialsId: 'order-credential-Id', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                        bat "docker login -u %DOCKERHUB_USERNAME% -p %DOCKERHUB_PASSWORD%"
                        bat 'docker build -t %DOCKERHUB_USERNAME%/service-registry:latest .'
                        bat 'docker push %DOCKERHUB_USERNAME%/service-registry:latest'
                    }
                }
            }
        }
        
        stage('Build & Push Customer Service') {
            steps {
                dir('Customer') {
                    echo 'Building Customer Service...'
                    bat 'mvn clean package -DskipTests'
                    withCredentials([usernamePassword(credentialsId: 'order-credential-Id', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
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
        
        /*stage('Deploy Ecosystem') {
            steps {
                echo 'Cleaning up any ghost containers...'                
                // The "|| echo" trick forces Jenkins to keep going even if the containers don't exist
                bat 'docker rm -f service-registry customer-service order-container || echo "Containers already clean"'
                
                echo 'Starting Discovery, Customer, Order, Kafka, and MySQL simultaneously...'
                bat 'docker-compose down'
                bat 'docker-compose up -d'
            }
        }*/
        // --- 4. DEPLOY TO AWS CLOUD ---
        // --- 4. DEPLOY TO AWS CLOUD ---
        stage('Deploy to AWS') {
            steps {
                echo 'Connecting to AWS EC2 Server...'
                
                // Bypassing the buggy plugin and using the native Windows SSH tool!
                // Make sure to update the path to wherever you saved your .pem file
                bat '''
                ssh -i "D:\\Tools\\aws-microservices-key.pem" -o StrictHostKeyChecking=no ubuntu@54.82.85.215 "cd E-CommerceBackend && git pull origin main && docker-compose down && docker-compose up -d"
                '''
            }
        }
    }
}