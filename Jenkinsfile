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

      try {
        sh('mvn test')
      } finally {
        junit('target/surefire-reports/*.xml')
      }
    }
  }
}
