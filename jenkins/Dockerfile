# Jenkins LTS + Docker CLI (with buildx and compose plugins) + Pipeline and Git plugins
FROM jenkins/jenkins:lts-jdk17
COPY --from=docker:27-cli /usr/local/bin/docker /usr/local/bin/docker
COPY --from=docker:27-cli /usr/local/libexec/docker/cli-plugins /usr/local/libexec/docker/cli-plugins
RUN jenkins-plugin-cli --plugins workflow-aggregator git
