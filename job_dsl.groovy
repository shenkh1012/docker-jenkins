#!groovy

folder("${APP_NAME}") {
}

pipelineJob("${APP_NAME}/build") {
  definition{
    cpsScm {
      scm {
        git {
          branch("develop")
          remote {
            credentials("${APP_GIT_USER_ID}")
            url("${APP_GIT_URL}")
          }
        }
      }
      scriptPath("Jenkinsfile_docker_build.groovy")
    }
  }
}

job("${APP_NAME}/qa-promote") {
  properties{
    promotions {
      promotion {
        name('Deploy QA')
        conditions {
          manual('jenkins') {
            parameters {
              booleanParam('DEPLOY_TO_QA', true, 'Deploy')
            }
          }
        }
        actions {
          downstreamParameterized {
            trigger("${APP_NAME}/qa-deploy") {
              block {
                buildStepFailure('FAILURE')
                failure('FAILURE')
                unstable('UNSTABLE')
              }
            }
          }
        }
      }
    }
  }
}

job("${APP_NAME}/qa-deploy") {
  steps {
    shell('echo "QA deploy"')
  }
}

deliveryPipelineView("${APP_NAME}/qa-deploy-view") {
  allowPipelineStart(true)
  columns(2)
  enableManualTriggers(true)
  linkToConsoleLog(true)
  pipelineInstances(5)
  showAggregatedPipeline(true)
  showAvatars(true)
  showChangeLog(true)
  showPromotions(true)
  showTestResults(true)
  sorting(Sorting.TITLE)
  updateInterval(60)
  pipelines {
    component("${APP_NAME}", "${APP_NAME}/build")
  }
}
