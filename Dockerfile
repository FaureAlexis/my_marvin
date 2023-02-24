FROM jenkins/jenkins:latest

ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"
ENV CASC_JENKINS_CONFIG=/var/jenkins_home/config.yml

COPY plugins.txt /var/jenkins_home/plugins.txt

RUN jenkins-plugin-cli --plugin-file /var/jenkins_home/plugins.txt

COPY my_marvin.yml /var/jenkins_home/config.yml
COPY job_dsl.groovy /var/jenkins_home/job_dsl.groovy
