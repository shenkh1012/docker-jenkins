#!groovy

node {
  stage('Init') {
    echo 'Init'
  }

  stage('Build') {
    checkout scm

    withDockerContainer('image': 'maven:3-jdk-8', 'args':'-v /root/.m2:/root/.m2') {
      sh 'mvn -B clean compile'
    }
  }

  stage('Test') {
    withDockerContainer('image': 'maven:3-jdk-8', 'args':'-v /root/.m2:/root/.m2') {
      sh 'mvn test'
    }
  }
}
