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
          image 'maven:3.6.0-jdk-8'
          args '-v /root/.m2:/root/.m2'
        }
      }

      steps {
        sh 'mvn -B -DskipTests clean package'
      }
    }

    stage('Test') {
      agent {
        docker {
          image 'maven:3.6.0-jdk-8'
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
  showEnvironmentVariables()
}

def showEnvironmentVariables() {
  // Print environment variables
  sh 'env | sort > env.txt'
  sh 'cat env.txt'
}
