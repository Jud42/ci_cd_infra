FROM jenkins/jenkins:2.462.1-jdk17
USER root
RUN apt-get update && apt-get install -y lsb-release ansible-core sshpass

RUN chown -R jenkins:jenkins /var/jenkins_home
RUN chmod -R 770 jenkins:jenkins /var/jenkins_home/ansible

# _certificat module ssl/tls
RUN ansible-galaxy collection install community.crypto

RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc \
  https://download.docker.com/linux/debian/gpg
RUN echo "deb [arch=$(dpkg --print-architecture) \
  signed-by=/usr/share/keyrings/docker-archive-keyring.asc] \
  https://download.docker.com/linux/debian \
  $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list
RUN apt-get update && apt-get install -y docker-ce-cli

RUN usermod -aG root jenkins
USER jenkins
RUN jenkins-plugin-cli --plugins "ansible docker-workflow"

