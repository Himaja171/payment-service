pipeline {
    agent any

    environment {
        APP_NAME        = 'payment-service'
        DOCKER_IMAGE    = "vmsigma/${APP_NAME}"
        NEXUS_URL       = 'nexus.vmsigma.internal:8082'
        SONAR_PROJECT   = 'payment-service'
        AWS_REGION      = 'ap-south-1'
        ECR_REPO        = "aws_account_id.dkr.ecr.${AWS_REGION}.amazonaws.com/${APP_NAME}"
    }

    tools {
        maven 'Maven-3.9'
        jdk   'JDK-17'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    credentialsId: 'github-credentials',
                    url: 'https://github.com/himajadudikatla/payment-service.git'
                echo "✅ Code checked out from GitHub"
            }
        }

        stage('Build & Unit Test') {
            steps {
                sh 'mvn clean package -DskipTests=false'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    jacoco execPattern: 'target/jacoco.exec'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh """
                        mvn sonar:sonar \
                          -Dsonar.projectKey=${SONAR_PROJECT} \
                          -Dsonar.host.url=http://sonarqube.vmsigma.internal:9000 \
                          -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    """
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
                echo "✅ SonarQube Quality Gate passed"
            }
        }

        stage('Upload Artifact to Nexus') {
            steps {
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: "${NEXUS_URL}",
                    groupId: 'com.vmsigma',
                    version: "${BUILD_NUMBER}",
                    repository: 'payment-service-releases',
                    credentialsId: 'nexus-credentials',
                    artifacts: [[
                        artifactId: "${APP_NAME}",
                        classifier: '',
                        file: "target/${APP_NAME}-1.0.0.jar",
                        type: 'jar'
                    ]]
                )
                echo "✅ Artifact uploaded to Nexus"
            }
        }

        stage('Docker Build & Push to ECR') {
            steps {
                script {
                    sh """
                        aws ecr get-login-password --region ${AWS_REGION} | \
                        docker login --username AWS --password-stdin ${ECR_REPO}

                        docker build -t ${APP_NAME}:${BUILD_NUMBER} .
                        docker tag ${APP_NAME}:${BUILD_NUMBER} ${ECR_REPO}:${BUILD_NUMBER}
                        docker tag ${APP_NAME}:${BUILD_NUMBER} ${ECR_REPO}:latest
                        docker push ${ECR_REPO}:${BUILD_NUMBER}
                        docker push ${ECR_REPO}:latest
                    """
                }
                echo "✅ Docker image pushed to AWS ECR"
            }
        }

        stage('Deploy to EKS') {
            steps {
                sh """
                    aws eks update-kubeconfig --region ${AWS_REGION} --name vmsigma-eks-cluster
                    sed -i 's|IMAGE_TAG|${BUILD_NUMBER}|g' k8s/deployment.yaml
                    kubectl apply -f k8s/deployment.yaml
                    kubectl apply -f k8s/service.yaml
                    kubectl rollout status deployment/${APP_NAME} -n production --timeout=120s
                """
                echo "✅ Deployed to EKS cluster"
            }
        }

    }

    post {
        success {
            echo "🎉 Pipeline completed successfully — Build #${BUILD_NUMBER}"
        }
        failure {
            echo "❌ Pipeline failed — Check logs for Build #${BUILD_NUMBER}"
            emailext(
                subject: "FAILED: ${APP_NAME} Build #${BUILD_NUMBER}",
                body: "Pipeline failed. Check Jenkins: ${BUILD_URL}",
                to: 'devops-team@vmsigma.com'
            )
        }
        always {
            cleanWs()
        }
    }
}
