pipeline {
  

  environment {
    CI = 'true'
  }

  stages {
    stage('Build') {
      agent {
        docker {
          image 'maven:3-alpine'
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
    
    stage('Deploy to Docker') {
      agent any
      
      steps {
        sh 'docker info'
      }
    }
  }
}
