#!groovy

def applicationBuilder

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
        applicationBuilder.buildApplication()
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
        applicationBuilder.runTests()
      }
    }

    stage('Build docker image') {
      agent any

      steps {
        applicationBuilder.runDockerImage()
      }
    }

    stage('Run docker image') {
      agent any

      steps {
        applicationBuilder.runDockerImage()
      }
    }
  }
}

def init() {
  applicationBuilder = new ApplicationBuilder()
}

class ApplicationBuilder {
  private String systemName
  private String applicationName
  private String branchName
  private String applicationVersion
  private String port
  private String imageName

  ApplicationBuilder() {
    init()
  }

  /**
   * Build application
   */
  void buildApplication() {
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
  void runTests() {
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
  void buildDockerImage() {
    if (this.branchName == 'master' || this.branchName == 'develop') {
      sh 'docker version'
      sh 'docker info'

      stopContainerIfExists()

      // TODO Delete old image files

      docker.build(this.imageName)

      // TODO Clean up dangling images?? (<none>:<none> images)
      // sh 'docker rmi $(docker images -f "dangling=true" -q)'
    } else {
      echo 'Skip build image step for branch: ' + this.branchName
    }
  }

  /**
   * Run docker image
   */
  void runDockerImage() {
    if (this.branchName == 'master' || this.branchName == 'develop') {
      // -d: Run docker image in deemon
      // --rm: Auto-remove docker container after stop
      sh 'docker run -d --rm -p ' + this.port + ':8080 ' + this.imageName
    } else {
      echo 'Skip run docker image step for branch: ' + this.branchName
    }
  }

  private void init() {
    echo 'Initial application builder...'

    this.systemName = 'kyle'
    this.applicationName = 'docker-jenkins'
    this.applicationVersion = '0.0.1-SNAPSHOT'
    this.branchName = env.BRANCH_NAME

    if (this.branchName == "master") {
      this.port = '8000'
    } else if (this.branchName == 'develop') {
      this.port = '8001'
    }

    this.imageName = this.systemName + "/" + this.applicationName + ':' + (this.branchName == 'master'? '' : this.branchName + '-')  + this.applicationVersion

    this.showBuildInfo()
  }

  private void showBuildInfo() {
    echo 'Build info:'
    echo 'System name: ' + this.systemName
    echo 'Application name: ' + this.applicationName
    echo 'Branch: ' + this.branchName
    echo 'Version: ' + this.systemName
    echo 'Image: ' + this.imageName
  }

  private void stopContainerIfExists() {
    // Get container id of the current running container
    def containerId = sh(returnStdout: true, script: "docker ps | grep '" + this.imageName + "' | awk '{print \$1;}'")
    echo 'Running containerId=' + containerId

    if (containerId.trim()) {
      // Stop container
      sh 'docker stop ' + containerId

      // Wait for stop
      sleep time: 5, unit: 'SECONDS'
    }
  }
}
