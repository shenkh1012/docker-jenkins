#!groovy

node {
  stage('init') {
    checkout(scm)
    echo 'Initial project......'
  }

  withDockerContainer("image" : "maven:3-jdk-8", "args" : "-v /root/.m2:/root/.m2") {
    stage('Maven - build') {
      echo 'Build project......'

      sh('mvn -B -DskipTests clean package')
      archiveArtifacts(artifacts: 'target/*.jar', fingerprint: true)
    }

    stage('Maven - test') {
      echo 'Run tests......'

      try {
        sh('mvn test')
      } finally {
        junit('target/surefire-reports/*.xml')
      }
    }

    stage('Maven - install') {
      echo 'Maven install'

      sh('mvn install')
    }
  }

  stage('Build docker image......') {
    echo 'Build docker image......'
  }

  stage('Run docker image......') {
    echo 'Run docker image......'
  }
}
