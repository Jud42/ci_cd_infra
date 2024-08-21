# ci_cd_jenkins_ansible

## Overview

This project automates the setup of a CI/CD pipeline using **Jenkins**, **Ansible**, and **Docker**.

**Part One**: The creation of the necessary environment, which includes the following services:

- **Nginx Server**: Configured via Ansible to act as a reverse proxy and ensure secure connections with TLS/SSL certificates.
- **Web Server**: Configured via Ansible to host the Java application.
- **Jenkins Server**: Set up via Docker with **Ansible** installed, along with the necessary **Jenkins plugins**.

**Part Two**: Installation and configuration of Jenkins, including the setup of the admin user and jobs. The pipeline consists of two jobs:

- **`build_app`** (Upstream): Handles building the application.
- **`deploy_app`** (Downstream): Manages the deployment of the application using Ansible.

> The Jenkinsfiles for these jobs are located on the `java_app` branch of the repository.

## Installation

> Stay on the `main` branch during installation and testing because the Ansible directory is shared with the `jenkins-ansible` container. Otherwise, the playbook in the container may be lost, which could cause issues during deployment.

### Prerequisites

- Docker and Docker Compose installed on your machine.

### Part One
#### Clone the Repository
```shell
git clone https://github.com/Jud42/ci_cd_infra.git
```
#### Create the containers
```shell
cd ci_cd_infra
./script.sh --create
```

### Part two
#### Retrieve the initial admin password for Jenkins
```shell
./script.sh --get-pass
```

#### Open Jenkins on Localhost and Create an Admin Account
**Jenkins URL:** http://localhost:8082  
> Copy and paste the initial admin password.

#### Add Maven
**Manage Jenkins => System Configuration/Tools => Maven installations => Add Maven**

![Maven installations](https://github.com/Jud42/ci_cd_infra/blob/main/.assets/Pasted%20image%2020240820195840.png)

#### Add Jobs

##### `build_app`
1. **New Item**
   - **Name:** `build_app`
   - **Type:** Pipeline
   - **Definition:** Pipeline script from SCM
   - **SCM:** Git
   - **Repository URL:** `https://github.com/Jud42/ci_cd_infra.git`
   - **Branches to build:** `*/java_app`
   - **Script Path:** `Jenkinsfile_build_app`

##### `deploy_app`
1. **New Item**
   - **Name:** `deploy_app`
   - **Type:** Pipeline
   - **Definition:** Pipeline script from SCM
   - **SCM:** Git
   - **Repository URL:** `https://github.com/Jud42/ci_cd_infra.git`
   - **Branches to build:** `*/java_app`
   - **Script Path:** `Jenkinsfile_deploy_app`

## Pipeline Stages
### build_app
```bash
+--------------------+     +--------------------+     +--------------------+     +--------------------+     +--------------------+     +--------------------+     +--------------------+     +--------------------+
|  Checkout SCM      | --> |  Tool Install      | --> |      Build         | --> |    Unit Tests      | --> | Integration Tests  | --> | Artifacts Upload   | --> | Trigger Deploy     | --> | Post Actions (Remove workspace) |
+--------------------+     +--------------------+     +--------------------+     +--------------------+     +--------------------+     +--------------------+     +--------------------+     +--------------------+
```
### deploy_app
```bash
+--------------------+     +--------------------+     +--------------------+     +--------------------+
|  Checkout SCM      | --> | Retrieve Artifact  | --> |      Deploy        | --> |   Post Actions  (Remove workspace)   |
+--------------------+     +--------------------+     +--------------------+     +--------------------+
```
## Project Structure
```bash
.  
├── ansible  
│   ├── 00_inventory.yml  
│   ├── group_vars  
│   │   ├── all.yml  
│   │   ├── nginx  
│   │   │   └── vars.yml  
│   │   └── webserver  
│   │       └── vars.yml  
│   ├── playbook.yml  
│   └── roles  
│       ├── nginx  
│       │   ├── defaults  
│       │   │   └── main.yml  
│       │   ├── handlers  
│       │   │   └── main.yml  
│       │   ├── meta  
│       │   │   └── main.yml  
│       │   ├── README.md  
│       │   ├── tasks  
│       │   │   └── main.yml  
│       │   ├── templates  
│       │   │   ├── default_vhost.conf.j2  
│       │   │   └── nginx.conf.j2  
│       │   ├── tests  
│       │   │   ├── inventory  
│       │   │   └── test.yml  
│       │   └── vars  
│       │       └── main.yml  
│       ├── ssh_keygen  
│       │   ├── defaults  
│       │   │   └── main.yml  
│       │   ├── handlers  
│       │   │   └── main.yml  
│       │   ├── meta  
│       │   │   └── main.yml  
│       │   ├── README.md  
│       │   ├── tasks  
│       │   │   └── main.yml  
│       │   ├── tests  
│       │   │   ├── inventory  
│       │   │   └── test.yml  
│       │   └── vars  
│       │       └── main.yml  
│       ├── tls_certificate  
│       │   ├── defaults  
│       │   │   └── main.yml  
│       │   ├── handlers  
│       │   │   └── main.yml  
│       │   ├── meta  
│       │   │   └── main.yml  
│       │   ├── README.md  
│       │   ├── tasks  
│       │   │   └── main.yml  
│       │   ├── tests  
│       │   │   ├── inventory  
│       │   │   └── test.yml  
│       │   └── vars  
│       │       └── main.yml  
│       ├── tomcat  
│       │   ├── defaults  
│       │   │   └── main.yml  
│       │   ├── files  
│       │   │   └── demo-0.0.1-SNAPSHOT-5.war  
│       │   ├── handlers  
│       │   │   └── main.yml  
│       │   ├── meta  
│       │   │   └── main.yml  
│       │   ├── README.md  
│       │   ├── tasks  
│       │   │   └── main.yml  
│       │   ├── templates  
│       │   │   ├── context.xml.j2  
│       │   │   ├── server.xml.j2  
│       │   │   ├── tomcat-users.xml.j2  
│       │   │   └── web.xml.j2  
│       │   ├── tests  
│       │   │   ├── inventory  
│       │   │   └── test.yml  
│       │   └── vars  
│       │       └── main.yml  
│       └── users  
│           ├── defaults  
│           │   └── main.yml  
│           ├── handlers  
│           │   └── main.yml  
│           ├── meta  
│           │   └── main.yml  
│           ├── README.md  
│           ├── tasks  
│           │   └── main.yml  
│           ├── tests  
│           │   ├── inventory  
│           │   └── test.yml  
│           └── vars  
│               └── main.yml  
├── docker_services  
│   ├── compose.yml  
│   ├── jenkins-ansible  
│   │   └── Dockerfile  
│   ├── nginx-reverse  
│   │   └── Dockerfile  
│   └── tomcat-webserv  
│       └── Dockerfile  
├── info_containers.txt  
├── README.md  
└── script.sh
```
