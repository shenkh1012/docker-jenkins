#!groovy

node {
  stage('Init') {
    echo 'Init'
  }

  stage('Build') {
    checkout scm

    withDockerContainer('image': 'maven:3-jdk-8') {
      sh 'mvn -B clean compile'
    }
  }
}
