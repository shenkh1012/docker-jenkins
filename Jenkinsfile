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
          image 'maven:3-jdk-8'
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
