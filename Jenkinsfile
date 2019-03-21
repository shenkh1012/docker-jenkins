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
        sh 'mvn test'
      }

      post {
        always {
          junit 'target/surefire-reports/*.xml'
        }
      }
    }

    stage('Build docker image') {
      agent any

      steps {
        buildDockerImage()
      }
    }
  }
}

def applicationBuilder = []

/**
 * Init build info
 * @return
 */
def init() {
  echo 'Initial application builder......'

  applicationBuilder.systemName = 'kyle'
  applicationBuilder.applicationName = 'docker-jenkins'
  applicationBuilder.applicationVersion = '0.0.1-SNAPSHOT'
  applicationBuilder.branchName = env.BRANCH_NAME

  if (applicationBuilder.branchName == "master") {
    applicationBuilder.port = '8000'
  } else if (applicationBuilder.branchName == 'develop') {
    applicationBuilder.port = '8001'
  }

  applicationBuilder.imageName = applicationBuilder.systemName + "/" + applicationBuilder.applicationName + ':' + (applicationBuilder.branchName == 'master'? '' : applicationBuilder.branchName + '-')  + applicationBuilder.applicationVersion

  showBuildInfo()
}

def showBuildInfo() {
  echo 'Build info:'
  echo 'System name: ' + applicationBuilder.systemName
  echo 'Application name: ' + applicationBuilder.applicationName
  echo 'Branch: ' + applicationBuilder.branchName
  echo 'Version: ' + applicationBuilder.systemName
  echo 'Image: ' + applicationBuilder.imageName
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
    junit 'target/surefire-reports/*.xml'
  }
}

/**
 * Build docker image
 */
def buildDockerImage() {
  if (applicationBuilder.branchName == 'master' || applicationBuilder.branchName == 'develop') {
    sh 'docker version'
    sh 'docker info'

    stopContainerIfExists()

    // TODO Delete old image files

    docker.build(applicationBuilder.imageName)

    // TODO Clean up dangling images?? (<none>:<none> images)
    // sh 'docker rmi $(docker images -f "dangling=true" -q)'
  } else {
    echo 'Skip build image step for branch: ' + applicationBuilder.branchName
  }
}

def stopContainerIfExists() {
  // Get container id of the current running container
  def containerId = sh(returnStdout: true, script: "docker ps | grep '" + applicationBuilder.imageName + "' | awk '{print \$1;}'")
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
void runDockerImage() {
  if (applicationBuilder.branchName == 'master' || applicationBuilder.branchName == 'develop') {
    // -d: Run docker image in deemon
    // --rm: Auto-remove docker container after stop
    sh 'docker run -d --rm -p ' + applicationBuilder.port + ':8080 ' + applicationBuilder.imageName
  } else {
    echo 'Skip run docker image step for branch: ' + applicationBuilder.branchName
  }
}

