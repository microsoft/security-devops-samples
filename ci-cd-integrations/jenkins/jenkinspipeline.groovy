pipeline {
    agent any
    environment {
        GDN_MDC_CLI_TENANT_ID=""                    // <-- Input MDC Tenant ID
        GDN_MDC_CLI_CLIENT_ID=""                    // <-- Input MDC Client ID
        GDN_MDC_CLI_CLIENT_SECRET=""                // <-- Input MDC Client Secret
        GDN_PIPELINENAME="jenkins"
        GDN_TRIVY_ACTION="image"
        GDN_TRIVY_TARGET="cli_jenkins_image"        // <-- should match the IMAGE_NAME
        DOCKER_REGISTRY="docker.io"
        DOCKER_USERNAME=""                          // <-- Input Docker username
        DOCKER_PASSWORD=""                          // <-- Input Docker password
        IMAGE_NAME="cli_jenkins_image"
    }
    stages {  
        stage('Clone') {
            steps {
                script {
                    node {
                        echo 'Cloning Repository...'
                        // Clone the GitHub repository
                        git branch: 'main', url: 'https://github.com/org/repo'
                    }
                }
            }
        }
        stage('Build Docker Container') {
            steps {
                script {
                    node {
                        echo 'Building Docker Container...'
                        // Build the Docker container
                        sh '''
                          docker build -t ${IMAGE_NAME} .
                          docker login ${DOCKER_REGISTRY} --username ${DOCKER_USERNAME} --password ${DOCKER_PASSWORD}
                          docker tag ${IMAGE_NAME} ${GDN_TRIVY_TARGET}:V${BUILD_NUMBER}
                          docker tag ${IMAGE_NAME} ${GDN_TRIVY_TARGET}:latest
                          docker push ${GDN_TRIVY_TARGET}:V${BUILD_NUMBER}
                        '''
                    }
                }
            }
        }
        stage ('Run Trivy & Upload') {
            steps {
                script {
                    node {
                        sh 'curl -L -o ./msdo_linux.zip "https://www.nuget.org/api/v2/package/Microsoft.Security.DevOps.Cli.linux-x64/"'
                        sh 'unzip -o ./msdo_linux.zip'
                        sh 'chmod +x tools/guardian'
                        sh 'chmod +x tools/Microsoft.Guardian.Cli'
                        sh 'tools/guardian init --force'
                        sh 'tools/guardian run -t trivy --export-file ./ubuntu-test.sarif --publish-file-folder-path ./ubuntu-test.sarif --not-break-on-detections'
                    }
                }
            }
        }
    }
}
