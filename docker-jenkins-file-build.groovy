#!groovy

def MAVEN_IMAGE = "maven:3-jdk-8"
def MAVEN_ARGS = "-v /root/.m2:/root/.m2"

node {
  stage('Init') {
    echo('Init')
    checkout scm
  }

  stage('Build') {
    withDockerContainer("image" : MAVEN_IMAGE, "args" : MAVEN_ARGS) {
      sh('mvn -B -DskipTests clean package spring-boot:repackage')
    }
  }

  stage('Test') {
    withDockerContainer("image" : MAVEN_IMAGE, "args" : MAVEN_ARGS) {
      try {
        sh('mvn test')
      } finally {
        junit('target/surefire-reports/*.xml')
      }
    }
  }
}

class BuildInfo {
  private static BuildInfo INSTANCE = new BuildInfo()

  private String applicationName
  private String branchName
  private String version

  private BuildInfo() {}

  static BuildInfo getInstance() {
    return INSTANCE
  }

  void init() {
    this.applicationName = "docker-jenkins"
    this.branchName = "${BRANCH_NAME}"
  }

  String getApplicationName() {
    return applicationName
  }

  String getBranchName() {
    return branchName
  }

  String getVersion() {
    return version
  }
}
