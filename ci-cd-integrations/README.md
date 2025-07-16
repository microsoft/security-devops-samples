# Defender for Cloud Command Line Interface (CLI)

Defender for Cloud Command Line Interface (CLI) is a tool designed for continuous integration and continuous deployment (CI/CD) pipelines. It performs static analysis and connects your code to cloud services. You can use it in any build process to scan container images for security vulnerabilities using built-in scanners. The results are sent to the Defender for Cloud portal, where the Cloud Security Explorer provides access to the container image and details about its vulnerabilities.

[For more detailed information](https://learn.microsoft.com/en-us/azure/defender-for-cloud/cli-cicd-integration)

* Authenticates with Microsoft Defender for Cloud
* Installs the latest Microsoft and 3rd party security tools
* Normalized processing of results into the SARIF format
* Uploads results to Microsoft Defender for Cloud

## Required environment variables for all Pipelines

| Name | Value |
| --- | --- | 
| GDN_MDC_CLI_CLIENT_ID | Generated MDC Client ID |
| GDN_MDC_CLI_CLIENT_SECRET | Generated Client Secret |
| GDN_MDC_CLI_TENANT_ID | Azure AD Tenant ID  |
| GDN_PIPELINENAME | `bitbucket` `jenkins` `gcp` `bamboo` `circle` `travis` `teamcity` `oci` or `aws` |

## Running Trivy

To run Trivy, you must first build the container. In your CI/CD tooling, add the following enviornment variables

Additional Environment Variables

| Name | Value |
| --- | --- |
| GDN_TRIVY_ACTION | image |
| GDN_TRIVY_TARGET | `<image name>` |

## Trademarks

This project may contain trademarks or logos for projects, products, or services. Authorized use of Microsoft 
trademarks or logos is subject to and must follow 
[Microsoft's Trademark & Brand Guidelines](https://www.microsoft.com/en-us/legal/intellectualproperty/trademarks/usage/general).
Use of Microsoft trademarks or logos in modified versions of this project must not cause confusion or imply Microsoft sponsorship.
Any use of third-party trademarks or logos are subject to those third-party's policies.

