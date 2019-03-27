#!groovy
def folderName = "docker-jenkins"
def gitUrl = "https://github.com/shenkh1012/docker-jenkins.git"
def gitUserId = "233bddb3-bd9c-46cc-9861-8e0ee8ae9f5b"

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
        credentials("${gitUserId}")
        url("${gitUrl}")
      }
    }
  }

  steps {
    jobDsl  {
      targets("seed_job_dsl.groovy")
    }
  }
}