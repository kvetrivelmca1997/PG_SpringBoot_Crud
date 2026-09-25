// Jenkins pipeline for the CRUD Operations app.
// Build & Test: the Dockerfile runs "mvn package", so the JUnit tests (on H2) run inside Docker.
// Deploy: a separate CI copy (project "ci-crud", port 9190) that never clashes with your own run on 9100.
pipeline {
  agent any

  triggers {
    pollSCM('H/2 * * * *')      // check GitHub about every 2 minutes, build only on new commits
  }

  options {
    buildDiscarder(logRotator(numToKeepStr: '10'))
  }

  environment {
    COMPOSE_PROJECT_NAME = 'ci-crud'
    APP_PORT = '9190'
  }

  stages {
    stage('Commit') {
      steps { sh 'git log -1 --oneline' }
    }
    stage('Build & Test') {
      steps { sh 'docker compose build' }
    }
    stage('Deploy') {
      steps { sh 'docker compose up -d' }
    }
    stage('Smoke Test') {
      steps {
        sh 'docker run --rm --network ci-crud_default curlimages/curl --retry 30 --retry-delay 3 --retry-all-errors -sf http://app:9100/actuator/health'
        sh 'docker run --rm --network ci-crud_default curlimages/curl -sf -o /dev/null http://app:9100/getallusers'
      }
    }
  }

  post {
    success { echo '🚀 Deployed! Open http://localhost:9190' }
    failure { sh 'docker compose logs --tail=80 app || true' }
    always  { sh 'docker compose ps' }
  }
}
