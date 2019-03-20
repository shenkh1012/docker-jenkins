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

    stage('Build docker image') {
      agent any

      steps {
        buildDockerImage()
      }
    }
  }
}

def init() {
  echo 'Initial of pipeline...'
  
  setEnvironmentVariables()
  
  showEnvironmentVariables()
}

def setEnvironmentVariables() {
  env.SYSTEM_NAME = 'kyle'
  env.APPLICATION_NAME = 'docker-jenkins'
  env.IMAGE_NAME = "${env.SYSTEM_NAME}/${env.APPLICATION_NAME}:" + ((env.BRANCH_NAME == "master") ? "" : "${env.BRANCH_NAME}-") + env.BUILD_ID
}

def buildApplication() {
  withDockerContainer("maven:3.5.0-jdk-8-alpine") { 
    sh 'mvn -B clean package'
  }
  archiveArtifacts '**/target/docker-jenkins-0.0.1-SNAPSHOT.jar'
  step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'] )
}

def showEnvironmentVariables() {
  // Print environment variables
  sh 'env | sort > env.txt'
  sh 'cat env.txt'
}

def buildDockerImage() {
  sh 'docker version'
  sh 'docker info'

  docker.build(env.IMAGE_NAME)
}
