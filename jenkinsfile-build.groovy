#!groovy

node {
  stage('init') {
    echo 'Initial project......'
    checkout(scm)
    init()
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

  stage('Build docker image') {
    echo 'Build docker image......'
    docker.build(env.IMAGE_NAME)
  }

  stage('Run docker image') {
    echo 'Run docker image......'
    sh("docker run -p ${APPLICATION_PORT}:8080 ${IMAGE_NAME}")
  }
}

def init() {
  env.IMAGE_NAME = "${env.SYSTEM_NAME}/${env.APPLICATION_NAME}:" + (env.BRANCH_NAME == "master" ? "latest" : "alpine")
  env.APPLICATION_PORT = (env.BRANCH_NAME == "master" ? "8000" : "8001")
}
