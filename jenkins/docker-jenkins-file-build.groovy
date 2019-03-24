#!groovy

node {
  stage('Init') {
    echo 'Init'
  }

  stage('Build') {
    dir('../') {
      withDockerContainer('image':'maven:3-jdk-8') {
        sh 'mvn -B compile'
      }
    }
  }
}
