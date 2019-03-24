#!groovy

pipeline {
  agent any

  stages {
    stage('Init') {
      steps {
        echo 'Init'
      }
    }

    stage('Build') {
      agent mavenImage()
      steps {
        sh 'mvn -B clean compile'
      }
    }

    stage('Test') {
      agent mavenImage()
      steps {
        sh 'mvn test'
      }
    }
  }
}

def mavenImage() {
  return {
    docker {
      image("maven:3-jdk-8")
      args("-v /root/.m2:/root/.m2")
    }
  } as Object
}
