#!groovy

folder("${FOLDER_NAME}") {
}

multibranchPipelineJob("${FOLDER_NAME}/build") {
  branchSources {
    git {
      remote("${APP_GIT_URL}")
      credentialsId("${APP_GIT_USER_ID}")
    }
  }

  factory {
    workflowBranchProjectFactory {
      scriptPath("Jenkinsfile_docker_build.groovy")
    }
  }
}

job("${FOLDER_NAME}/qa-promote") {
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
            trigger("${FOLDER_NAME}/qa-deploy") {
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

job("${FOLDER_NAME}/qa-deploy") {
  steps {
    shell('echo "QA deploy"')
  }
}

deliveryPipelineView("${FOLDER_NAME}/qa-promote-view") {
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
    component("${FOLDER_NAME}", "${FOLDER_NAME}/qa-promote")
  }
}
