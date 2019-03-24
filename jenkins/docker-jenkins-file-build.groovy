#!groovy

node {
  stage('Init') {
    echo 'Init'
  }

  stage('Build') {
    withDockerContainer('image': 'maven:3-jdk-8') {
      dir('../') {
        pwd
        sh 'mvn -B compile'
      }
    }
  }
}
