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
  withDockerContainer("maven:3.6.0-jdk-8", "-v /root/.m2:/root/.m2") {
    sh "mvn clean package"
  }
  archiveArtifacts '**/target/ks-jenkins-docker-0.0.1-SNAPSHOT.jar'
  step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
}
