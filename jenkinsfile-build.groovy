#!groovy

node {
  stage('init') {
    checkout(scm)
    echo 'Initial project......'
  }

  withDockerContainer("image" : env.MAVEN_IMAGE, "args" : env.MAVEN_ARGS) {
    stage('Maven - build') {
      echo 'Build project......'

      sh('mvn -B -DskipTests clean package')
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
