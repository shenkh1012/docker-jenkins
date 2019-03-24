#!groovy

node {
  stage('Init') {
    echo 'Init'
  }

  stage('Build') {
    sh 'pwd'

    withDockerContainer('image': 'maven:3-jdk-8') {
      dir('../') {
        echo 'Building...'
        sh 'pwd'
        sh 'mvn -B compile'
      }
    }
  }
}
