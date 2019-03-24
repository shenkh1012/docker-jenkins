#!groovy

def MAVEN_IMAGE = "maven:3-jdk-8"
def MAVEN_ARGS = "-v /root/.m2:/root/.m2"
def buildInfo = null

node {
  stage('Init') {
    echo('Init')
    buildInfo = BuildInfo.instance

    checkout(scm)
  }

  stage('Build') {
    withDockerContainer("image" : MAVEN_IMAGE, "args" : MAVEN_ARGS) {
      try {
        sh('mvn -B -DskipTests clean package spring-boot:repackage')
      } finally {
        archiveArtifacts(artifacts: 'target/*.jar', fingerprint: true)
      }
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

  stage('Deploy') {
    echo('Deploy')
  }
}

class BuildInfo {
  private static BuildInfo INSTANCE = new BuildInfo()

  private String applicationName
  private String branchName
  private String version

  private BuildInfo() {
    init()
  }

  static BuildInfo getInstance() {
    return INSTANCE
  }

  private void init() {
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
