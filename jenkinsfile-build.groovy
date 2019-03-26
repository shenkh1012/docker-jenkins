#!groovy

node {
  stage('init') {
    echo 'Initial project......'
  }

  stage('Maven - build') {
    echo 'Build project......'
  }

  stage('Maven - test') {
    echo 'Run tests......'
  }

  stage('Maven - install') {
    echo 'Install maven......'
  }

  stage('Build docker image......') {
    echo 'Build docker image......'
  }

  stage('Run docker image......') {
    echo 'Run docker image......'
  }
}
