pipeline {
    agent any
    
    tools {
        maven 'Maven_3_9_9' 
        jdk 'jdk-21'
    }
    //Adding this line to limit the size 
    environment {
        // Limit Maven's RAM usage to 512MB per build so your laptop doesn't crash
        MAVEN_OPTS = '-Xmx512m'
        // Create a universal variable for your Docker username
        DOCKER_USER = 'nishantkalia13' 
    }
    
    stages {
		
        stage('Checkout') {
            steps {
                echo 'Downloading entire E-Commerce Backend from GitHub...'
                checkout scm
            }
        }
        
        // --- NEW: GLOBAL DOCKER LOGIN  ONCE WILL LOGIN DOESN'T REQUIRE EVERYTIME---
        stage('Login to Docker') {
            steps {
                echo 'Authenticating with DockerHub...'
                withCredentials([usernamePassword(credentialsId: 'order-credential-Id', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                    //bat 'docker login -u %DOCKERHUB_USERNAME% -p %DOCKERHUB_PASSWORD%'
                   // The secure way: We "echo" the password and pipe (|) it directly into Docker's hidden input channel
                    bat 'echo %DOCKERHUB_PASSWORD%| docker login -u %DOCKERHUB_USERNAME% --password-stdin'                
                }
            }
        }
        
        // --- FAST SEQUENTIAL BUILDS ---
        // Running these sequentially on a laptop is FASTER and safer than parallel
        stage('Build Infrastructure Services') {
            steps {
                dir('config_server') {
                    echo 'Building Config Server...'
                    // Pointing to a shared local cache stops Maven from re-downloading the internet
                   bat 'mvn clean package -DskipTests -Dmaven.repo.local=C:\\Users\\Dell\\.m2\\repository'
                    bat "docker build -t ${DOCKER_USER}/config-server:latest ."
                    bat "docker push ${DOCKER_USER}/config-server:latest"
                }
                dir('ServiceRegistry') {
                    echo 'Building Discovery Server...'
                   bat 'mvn clean package -DskipTests -Dmaven.repo.local=C:\\Users\\Dell\\.m2\\repository'
                    bat "docker build -t ${DOCKER_USER}/service-registry:latest ."
                    bat "docker push ${DOCKER_USER}/service-registry:latest"
                }
                dir('Gateway') {
                   bat 'mvn clean package -DskipTests -Dmaven.repo.local=C:\\Users\\Dell\\.m2\\repository'
                    bat "docker build -t ${DOCKER_USER}/api-gateway:latest ."
                    bat "docker push ${DOCKER_USER}/api-gateway:latest"
                }
            }
        }
       stage('Build Core Services') {
            steps {
                dir('auth-service') {
                   bat 'mvn clean package -DskipTests -Dmaven.repo.local=C:\\Users\\Dell\\.m2\\repository'
                    bat "docker build -t ${DOCKER_USER}/auth-service:latest ."
                    bat "docker push ${DOCKER_USER}/auth-service:latest"
                }
                dir('Customer') {
                   bat 'mvn clean package -DskipTests -Dmaven.repo.local=C:\\Users\\Dell\\.m2\\repository'
                    bat "docker build -t ${DOCKER_USER}/customer-service:latest ."
                    bat "docker push ${DOCKER_USER}/customer-service:latest"
                }
                dir('Product') {
                   bat 'mvn clean package -DskipTests -Dmaven.repo.local=C:\\Users\\Dell\\.m2\\repository'
                    bat "docker build -t ${DOCKER_USER}/product-service:latest ."
                    bat "docker push ${DOCKER_USER}/product-service:latest"
                }
                dir('Order') {
                   bat 'mvn clean package -DskipTests -Dmaven.repo.local=C:\\Users\\Dell\\.m2\\repository'
                    bat "docker build -t ${DOCKER_USER}/order-service:latest ."
                    bat "docker push ${DOCKER_USER}/order-service:latest"
                }
                dir('Payment') {
                   bat 'mvn clean package -DskipTests -Dmaven.repo.local=C:\\Users\\Dell\\.m2\\repository'
                    bat "docker build -t ${DOCKER_USER}/payment-service:latest ."
                    bat "docker push ${DOCKER_USER}/payment-service:latest"
                }
                dir('Notification-Service') {
                   bat 'mvn clean package -DskipTests -Dmaven.repo.local=C:\\Users\\Dell\\.m2\\repository'
                    bat "docker build -t ${DOCKER_USER}/notification-service:latest ."
                    bat "docker push ${DOCKER_USER}/notification-service:latest"
                }
            }
        }
        
        stage('Deploy Ecosystem Locally') {
            steps {
                echo 'Starting entire E-Commerce Architecture on Local Docker...'
                bat 'docker-compose down --remove-orphans'
                bat 'docker-compose up -d'
            }
        }
        
        /*stage('Deploy Ecosystem') {
            steps {
                echo 'Cleaning up any ghost containers...'                
                // The "|| echo" trick forces Jenkins to keep going even if the containers don't exist
                bat 'docker rm -f config-server service-registry gateway-service auth-service customer-service order-container product-service payment-service notification-service|| echo "Containers already clean"'
                
                echo 'Starting Discovery, Customer, Order, Kafka, and MySQL simultaneously...'
                bat 'docker-compose down'
                bat 'docker-compose up -d'
            }
        }*/
        // --- 4. DEPLOY TO AWS CLOUD ---
        // --- 4. DEPLOY TO AWS CLOUD ---
      /*  stage('Deploy to AWS') {
            steps {
                echo 'Connecting to AWS EC2 Server...'
                
                // Bypassing the buggy plugin and using the native Windows SSH tool!
                // Make sure to update the path to wherever you saved your .pem file
                bat '''
                ssh -i "D:\\Tools\\aws-microservices-key.pem" -o StrictHostKeyChecking=no ubuntu@18.213.2.127 "cd E-CommerceBackend && git pull origin main && docker-compose down && docker-compose pull && docker-compose up -d"
                '''
            }
        }*/
        
        
    }
    // --- FULLY AUTOMATED SYSTEM CLEANUP ---
    post {
        always {
            echo 'Pipeline finished! Running deep clean to protect D: drive...'   
                     
            // 1. Clear out dead/failed containers from mid-build crashes
            bat 'docker container prune -f'            
            
            // 2. Remove all unnamed, dangling ghost images
            bat 'docker image prune -f'
            
            // 3. Wipe the hidden build cache
            bat 'docker builder prune -a -f'
            
            echo 'Deep clean complete. Your system memory and storage are safe.'
            
            // 2. NEW: Jenkins Workspace Cleanup
            // This deletes the downloaded GitHub code and compiled .jar files
            cleanWs() 
            
            echo 'Deep clean complete. System memory is safe.'
            
            
        }
    }
}