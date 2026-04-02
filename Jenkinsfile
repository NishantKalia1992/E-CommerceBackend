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
        
       /* INFRASTRUCTURE SERVICES-----------*/
       
        stage('Build & push Config Server'){
			steps{
				dir('config_server'){
					echo 'Building Config server...'
					bat 'mvn clean package -DskipTests'
					withCredentials([usernamePassword(credentialsId: 'order-credential-Id', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
						bat "docker login -u %DOCKERHUB_USERNAME% -p %DOCKERHUB_PASSWORD%"
						bat 'docker build -t %DOCKERHUB_USERNAME%/config-server:latest .'
						bat 'docker push %DOCKERHUB_USERNAME%/config-server:latest'
					}
				}
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
        
        stage('Build & push API gateway & Auth Service') {
			steps {
				dir('Gateway') {
					echo 'Building Api Gateway Service...'
					bat 'mvn clean package -DskipTests'
					withCredentials([usernamePassword(credentialsId: 'order-credential-Id', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
						bat "docker login -u %DOCKERHUB_USERNAME% -p %DOCKERHUB_PASSWORD%"
						bat 'docker build -t %DOCKERHUB_USERNAME%/gateway-service:latest .'
						bat 'docker push %DOCKERHUB_USERNAME%/gateway-service:latest'
					}
				}
				dir('auth-service') {
					echo 'Building Auth Service...'
					bat 'mvn clean package -DskipTests'
					withCredentials([usernamePassword(credentialsId: 'order-credential-Id', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
						bat "docker login -u %DOCKERHUB_USERNAME% -p %DOCKERHUB_PASSWORD%"
						bat 'docker build -t %DOCKERHUB_USERNAME%/auth-service:latest .'
						bat 'docker push %DOCKERHUB_USERNAME%/auth-service:latest'
					}
				}
			}
		}
		
//		CORE MICROSERVICES ---------------------
        
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
        
        stage('Build & push Product service') {
			steps {
				dir('Product') {
					echo 'Building Product Service...'
                    bat 'mvn clean package -DskipTests'
					withCredentials([usernamePassword(credentialsId: 'order-credential-Id', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
						bat 'docker build -t %DOCKERHUB_USERNAME%/product-service:latest .'
						bat 'docker push %DOCKERHUB_USERNAME%/product-service:latest'
					}
				}
			}
		}
		
		stage('Build & push Payment Service') {
			steps {
				dir('Payment') {
					echo 'mvn clean package -DskipTests'
                    bat 'mvn clean package -DskipTests'
					withCredentials([usernamePassword(credentialsId: 'order-credential-Id', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
						bat 'docker build -t %DOCKERHUB_USERNAME%/payment-service:latest .'
						bat 'docker push %DOCKERHUB_USERNAME%/payment-service:latest'
					}
				}
			}
		}
		
		stage('Build & Push Notification Service') {
			steps {
				dir('Notification-Service') {
					echo 'mvn clean package -DskipTests'
                    bat 'mvn clean package -DskipTests'
					withCredentials([usernamePassword(credentialsId: 'order-credential-Id', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
						bat 'docker build -t %DOCKERHUB_USERNAME%/notification-service:latest .'
						bat 'docker push %DOCKERHUB_USERNAME%/notification-service:latest'
					}
				}
			}
		}
        
        stage('Deploy Ecosystem') {
            steps {
                echo 'Cleaning up any ghost containers...'                
                // The "|| echo" trick forces Jenkins to keep going even if the containers don't exist
                bat 'docker rm -f config-server service-registry gateway-service auth-service customer-service order-container product-service payment-service notification-service|| echo "Containers already clean"'
                
                echo 'Starting Discovery, Customer, Order, Kafka, and MySQL simultaneously...'
                bat 'docker-compose down'
                bat 'docker-compose up -d'
            }
        }
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
}