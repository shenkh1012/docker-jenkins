#!groovy

node {
  stage('Init') {
    echo 'Initial application build info......'

    env.MAVEN_IMAGE = "maven:3-jdk-8"
    env.MAVEN_ARGS = "-v /root/.m2:/root/.m2"

    env.SYSTEM_NAME = 'kyle'
    env.APPLICATION_NAME = 'docker-jenkins'
    env.APPLICATION_VERSION = '0.0.1-SNAPSHOT'
    env.IMAGE_NAME = "${env.SYSTEM_NAME}/${env.APPLICATION_NAME}:" + (env.BRANCH_NAME == "master" ? "latest" : "alpine")
    env.APPLICATION_PORT = (env.BRANCH_NAME == "master" ? "8000" : "8001")
  }

  withDockerContainer("image" : env.MAVEN_IMAGE, "args" : env.MAVEN_ARGS) {
    stage('Build') {
      checkout(scm)
      sh('mvn -B -DskipTests clean package spring-boot:repackage')
      archiveArtifacts(artifacts: 'target/*.jar', fingerprint: true)
    }

    stage('Test') {
      try {
        sh('mvn test')
      } finally {
        junit('target/surefire-reports/*.xml')
      }
    }
  }

  stage('Build docker image') {
    docker.build(env.IMAGE_NAME)
  }

  if (env.BRANCH_NAME == "master" || env.BRANCH_NAME == "develop") {
    stage('Run docker image') {
      // -d: Run docker image in daemon
      // --rm: Auto-remove docker container after stop
      sh('docker run -d --rm -p ' + env.APPLICATION_PORT + ':8080 ' + env.IMAGE_NAME)
    }
  }

}

