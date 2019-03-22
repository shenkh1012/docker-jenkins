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
        buildApplication()
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
        runTests()
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

/**
 * Init build info
 * @return
 */
def init() {
  echo 'Initial application builder......'

  env.SYSTEM_NAME = 'kyle'
  env.APPLICATION_NAME = 'docker-jenkins'
  env.APPLICATION_VERSION = '0.0.1-SNAPSHOT'

  if (env.BRANCH_NAME == "master") {
    env.APPLICATION_PORT = '8100'
  } else if (env.BRANCH_NAME == 'develop') {
    env.APPLICATION_PORT = '8101'
  }

  env.imageName = env.SYSTEM_NAME + "/" + env.APPLICATION_NAME + ':' + (env.BRANCH_NAME == 'master'? '' : env.BRANCH_NAME + '-')  + env.APPLICATION_VERSION

  showBuildInfo()
}

def showBuildInfo() {
  echo 'Build info:'
  echo 'System name: ' + env.SYSTEM_NAME
  echo 'Application name: ' + env.APPLICATION_NAME
  echo 'Version: ' + env.SYSTEM_NAME
  echo 'Image: ' + env.imageName
}

/**
 * Build application
 */
def buildApplication() {
  // Build application with maven and repackage with spring-boot
  try {
    sh 'mvn -B -DskipTests clean package spring-boot:repackage'
  } finally {
    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
  }
}

/**
 * Run test cases
 */
def runTests() {
  try {
    // Run tests with maven.
    sh 'mvn test'
  } finally {
    junit 'target/surefire-reports/TEST-*.xml'
  }
}

/**
 * Build docker image
 */
def buildDockerImage() {
  if (env.branchName == 'master' || env.branchName == 'develop') {
    sh 'docker version'
    sh 'docker info'

    stopContainerIfExists()

    // TODO Delete old image files

    docker.build(env.imageName)

    try {
      // Clean up dangling images (<none>:<none> images).
      sh 'docker rmi $(docker images -f "dangling=true" -q)'
    } catch (ignore) {
      // That's fine.
    }
  } else {
    echo 'Skip build image step for branch: ' + env.branchName
  }
}

def stopContainerIfExists() {
  // Get container id of the current running container
  def containerId = sh(returnStdout: true, script: "docker ps | grep '" + env.imageName + "' | awk '{print \$1;}'")
  echo 'Running containerId=' + containerId

  if (containerId.trim()) {
    // Stop container
    sh 'docker stop ' + containerId

    // Wait for stop
    sleep time: 5, unit: 'SECONDS'
  }
}

/**
 * Run docker image
 */
def runDockerImage() {
  if (env.branchName == 'master' || env.branchName == 'develop') {
    // -d: Run docker image in deemon
    // --rm: Auto-remove docker container after stop
    sh 'docker run -d --rm -p ' + env.APPLICATION_PORT + ':8080 ' + env.imageName
  } else {
    echo 'Skip run docker image step for branch: ' + env.branchName
  }
}
