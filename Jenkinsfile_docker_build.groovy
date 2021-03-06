#!groovy

node {
  stage('Init') {
    echo 'Init pipeline ......'

    checkout(scm)

    init()
  }

  withDockerContainer("image": "maven:3.6.0-jdk-8", "args": "-v /root/.m2:/root/.m2") {
    stage('Build') {
      echo 'Build project ......'

      // Build application with maven
      sh 'mvn -B -DskipTests clean package'
      archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
    }

    stage('Test') {
      echo 'Run tests ......'

      try {
        // Run tests with maven.
        sh 'mvn test'
      } finally {
        junit 'target/surefire-reports/TEST-*.xml'
      }
    }

    if (env.BRANCH_NAME == "master" || env.BRANCH_NAME == "develop") {
      stage('Install') {
        echo 'Install maven project ......'

        sh 'mvn -DskipTests install'
      }
    }
  }

  if (env.BRANCH_NAME == "master" || env.BRANCH_NAME == "develop") {
    stage("Build-docker-image") {
      if (branch("master") || branch("develop")) {
        echo 'Build docker image ......'

        sh 'docker info'

        stopContainerIfExists()

        docker.build(env.IMAGE_NAME)

        removeDanglingImages()
      }
    }

    stage("Run-docker-image") {
      if (branch("master") || branch("develop")) {
        echo 'Run docker image ......'

        sh "docker run -d --rm -p ${APPLICATION_PORT}:8080 ${IMAGE_NAME}"
      }
    }
  }

  if (env.BRANCH_NAME == "develop") {
    stage("QA-promote") {
      echo 'QA promote ......'

      build(job: "${APPLICAITON_NAME}/qa-promote", wait: false)
    }
  }
}

def init() {
  env.APPLICAITON_NAME = 'docker-jenkins'
  env.SYSTEM_NAME = 'kyle'
  env.IMAGE_NAME = "${env.SYSTEM_NAME}/${env.APPLICAITON_NAME}:" + (env.BRANCH_NAME == "master" ? "latest" : "alpine")
  env.APPLICATION_PORT = (env.BRANCH_NAME == "master" ? "8000" : "8001")
}

def stopContainerIfExists() {
  // Get container id of the current running container
  def containerId = sh(returnStdout: true, script: "docker ps | grep '${IMAGE_NAME}' | awk '{print \$1;}'")
  echo 'Running containerId=' + containerId

  if (containerId.trim()) {
    // Stop container
    sh 'docker stop ' + containerId

    // Wait for stop
    sleep time: 5, unit: 'SECONDS'
  }
}

def removeDanglingImages() {
  try {
    // Clean up dangling images (<none>:<none> images).
    sh 'docker rmi $(docker images -f "dangling=true" -q)'
  } catch (ignore) {
    // That's fine.
  }
}
