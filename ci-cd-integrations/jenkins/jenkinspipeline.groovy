pipeline {
    agent any
    environment {
        /* Defender for Cloud (Microsoft Security DevOps) */
        GDN_MDC_CLI_TENANT_ID     = credentials('MDC-TenantID')           
        GDN_MDC_CLI_CLIENT_ID     = credentials('MDC-CLI-ID')            
        GDN_MDC_CLI_CLIENT_SECRET = credentials('MDC-CLI-Secret')    
        GDN_PIPELINENAME          = "jenkins"
        GDN_TRIVY_ACTION          = "image"

        /* Registry details */
        REGISTRY        = "reg.azurecr.io"
        IMAGE_NAME      = "cli_jenkins_image"
        REGISTRY_CREDS  = credentials('Registry-Creds')          
    }
    stages {  
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/org/repo'   // <-- Input GitHub repository
            }
        }
        
        stage('Build & Push Container') {
            steps {
                script {
                    def commit  = env.GIT_COMMIT?.take(7) ?: 'unk'
                    def fullImg = "${env.REGISTRY}/${env.IMAGE_NAME}"
                    def verTag  = "${BUILD_NUMBER}-${commit}"
        
                    sh """
                        set -euo pipefail
                        docker build -t ${fullImg}:${verTag} -t ${fullImg}:latest .
        
                        echo "\${REGISTRY_CREDS_PSW}" | \
                            docker login ${env.REGISTRY} -u "\${REGISTRY_CREDS_USR}" --password-stdin
        
                        docker push ${fullImg}:${verTag}
                        if [ "\${BRANCH_NAME:-}" = "main" ]; then docker push ${fullImg}:latest; fi
                    """
        
                    env.GDN_TRIVY_TARGET = fullImg
                    env.IMAGE_TAG        = verTag
                }
            }
        }
        
        stage ('Scan with Trivy & Publish to MDC') {
            steps {
                script {
                    sh '''
                        set -euo pipefail
                        # Download the tool only if it isn’t cached on this agent
                        if [ ! -x tools/guardian ]; then
                          curl -sSL -o msdo_linux.zip \
                             "https://www.nuget.org/api/v2/package/Microsoft.Security.DevOps.Cli.linux-x64/"
                          unzip -oq msdo_linux.zip
                          chmod +x tools/guardian tools/Microsoft.Guardian.Cli
                        fi

                        tools/guardian init --force
                        tools/guardian run \
                            -t trivy \
                            --image ${GDN_TRIVY_TARGET}:${IMAGE_TAG} \
                            --export-file ./security-scan.sarif \
                            --publish-file ./security-scan.sarif \
                            --not-break-on-detections
                    '''
                }
            }
        }
    }
    
    post {
        success {
            archiveArtifacts artifacts: 'security-scan.sarif', fingerprint: true
        }
        always { 
            cleanWs()  // keep agent disk tidy
        }  
    }
}
