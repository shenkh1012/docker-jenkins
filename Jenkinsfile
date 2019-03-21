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
      agent {
        docker {
          image 'maven:3-alpine'
          args '-v /root/.m2:/root/.m2'
        }
      }

      steps {
        sh 'mvn -B -DskipTests clean package spring-boot:repackage'
      }
      
      post {
        always {
          archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
      }
    }
    
    stage('Test') {
      agent {
        docker {
          image 'maven:3-alpine'
          args '-v /root/.m2:/root/.m2'
        }
      }

      steps {
        sh 'mvn test'
      }

      post {
        always {
          junit 'target/surefire-reports/*.xml'
        }
      }
    }

    stage('Build docker image') {
      agent any

      steps {
        buildDockerImage()
      }
    }

    stage('Run docker image') {
      agent any

      steps {
        runDockerImage()
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
  env.APPLICATION_VERSION = '0.0.1-SNAPSHOT'
  env.IMAGE_NAME = "${env.SYSTEM_NAME}/${env.APPLICATION_NAME}:" + ((env.BRANCH_NAME == "master") ? "" : "${env.BRANCH_NAME}-") + env.APPLICATION_VERSION.toLowerCase()
}

def showEnvironmentVariables() {
  // Print environment variables
  sh 'env | sort > env.txt'
  sh 'cat env.txt'
}

def buildDockerImage() {
  sh 'docker version'
  sh 'docker info'

  stopContainerIfExists()

  deleteOldImageIfExists()

  sh 'docker build --tag ' + env.IMAGE_NAME
}

def stopContainerIfExists() {
  def containerId = sh(returnStdout: true, script: "docker ps | grep '${env.IMAGE_NAME}' | awk '{print \$1;}'")

  echo 'Running containerId=' + containerId

  if (containerId.trim()) {
    sh 'docker stop ' + containerId

    sleep time: 5, unit: 'SECONDS'
  }
}

def deleteOldImageIfExists() {
  def imageId = sh(returnStdout: true, script: "docker image | grep '${env.IMAGE_NAME}' | awk '{print \$3;}'")

  echo 'Old imageId=' + imageId

  if (imageId.trim()) {
    sh 'docker rmi ' + imageId

    sleep time: 2, unit: 'SECONDS'
  }
}


def runDockerImage() {
  sh 'docker run -d --rm -p 8001:8080 ' + env.IMAGE_NAME
}
