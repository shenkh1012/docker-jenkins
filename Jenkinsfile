pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args '-v /root/.m2:/root/.m2'
    }
  }

  environment {
    CI = 'true'
  }

  stages {
    stage('Build') {
      steps {
        sh 'mvn -B -DskipTests clean package'
      }
    }

    stage('Test') {
      steps {
        sh 'mvn test'
      }

      post {
        always {
          junit 'target/surefire-reports/*.xml'
        }
      }
    }

    stage('Deploy') {
      steps {
        sh 'chmod +x ./jenkins/scripts/run-app-in-docker.sh'
        sh './jenkins/scripts/run-app-in-docker.sh'
      }
    }
  }
}
