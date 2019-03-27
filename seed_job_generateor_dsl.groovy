#!groovy
def folderName = "docker-jenkins"
def gitUrl = "https://github.com/shenkh1012/docker-jenkins.git"
def gitUserId = "a2a87c7c-e0e2-45dc-bb1e-3a0bc4bb401a"

job ("seed-job") {
  parameters {
    stringParam("FOLDER_NAME", "${folderName}", "The folder name of the project.")
    stringParam("APP_GIT_URL", "${gitUrl}", "The git repository url of the application.")
    stringParam("APP_GIT_USER_ID", "${gitUserId}", "The user credentials ID of the user to login the git repository.")
  }

  scm {
    git {
      branch("develop")
      remote {
        credentials("${APP_GIT_USER_ID}")
        url("${APP_GIT_URL}")
      }
    }
  }

  steps {
    jobDsl  {
      targets("seed_job_dsl.groovy")
    }
  }
}