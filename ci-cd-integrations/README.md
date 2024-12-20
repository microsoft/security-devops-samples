# Defender for Cloud Command Line Interface (CLI)

Defender for Cloud Command Line Interface (CLI) is an application you can use in continuous integration and continuous deployment (CI/CD) pipelines. It runs static analysis tools and connects code to cloud services. You can use Defender for Cloud CLI in any build process to scan images for security vulnerabilities with built-in security scanners. It sends the scan results to the Defender for Cloud portal. The Cloud Security Explorer can then access the container image and its vulnerabilities.

[For more detailed information](https://learn.microsoft.com/en-us/azure/defender-for-cloud/cli-cicd-integration)

* Authenticates with Microsoft Defender for Cloud
* Installs the latest Microsoft and 3rd party security tools
* Normalized processing of results into the SARIF format
* Uploads results to Microsoft Defender for CLoud

## Required enviornment variables for all Pipelines

| Name | Value |
| --- | --- | 
| GDN_MDC_CLI_TENANT_ID | MDC Tenant ID |
| GDN_MDC_CLI_CLIENT_ID | MDC CLient ID |
| GDN_MDC_CLI_CLIENT_SECRET | MDC Client Secret |
| GDN_MDC_CLI_TENANT_ID | MDC Tenant ID |
| GDN_PIPELINENAME | jenkins, bitbucket |

## Running Trivy

To run Trivy, you must first build the container. In your CI/CD tooling, add the following enviornment variables

Additional Enviornment Variables

| Name | Value |
| --- | --- |
| GDN_TRIVY_ACTION | image |
| GDN_TRIVY_TARGET | image name |

## Trademarks

This project may contain trademarks or logos for projects, products, or services. Authorized use of Microsoft 
trademarks or logos is subject to and must follow 
[Microsoft's Trademark & Brand Guidelines](https://www.microsoft.com/en-us/legal/intellectualproperty/trademarks/usage/general).
Use of Microsoft trademarks or logos in modified versions of this project must not cause confusion or imply Microsoft sponsorship.
Any use of third-party trademarks or logos are subject to those third-party's policies.

