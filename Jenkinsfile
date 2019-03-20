#!groovy

pipeline {
  agent any

  stages {
    stage('Init') {
      steps {
        init()
      }
    }

    stage('Build Application') {
      agent any
      steps {
        buildApplication()
      }
    }
  }
}

def init() {
  echo 'Initial of pipeline...'
  showEnvironmentVariables()
}

def showEnvironmentVariables() {
  // Print environment variables
  sh 'env | sort > env.txt'
  sh 'cat env.txt'
}

def buildApplication() {
  withDockerContainer(image: "maven:3.6.0-jdk-8", args: "-v /root/.m2:/root/.m2") {
    sh "mvn clean package"
  }
}
