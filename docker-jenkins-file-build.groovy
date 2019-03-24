#!groovy

def MAVEN_IMAGE = "maven:3-jdk-8"
def MAVEN_ARGS = "-v /root/.m2:/root/.m2"
def buildInfo = [
    "branchName" : ""
]

node {
  stage('Init') {
    echo('Init build info')
    init()
    echo(buildInfo.branchName)

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

def init() {
  buildInfo.put("branchName", "${env.BRANCH_NAME}")
}
