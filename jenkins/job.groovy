folder('docker-jenkins') {
  description('Folder for sample project of CI/CD with Jenkins and Docker')
}

job('docker-jenkins/build') {
  steps {
    shell 'echo "build"'
  }
}

job('docker-jenkins/test') {
  triggers {
    upstream('docker-jenkins/build', 'SUCCESS')
  }

  steps {
    shell 'echo "test"'
  }
}

job('docker-jenkins/deploy') {
  triggers {
    upstream('docker-jenkins/test', 'SUCCESS')
  }

  steps {
    shell 'echo "deploy"'
  }
}

job('docker-jenkins/qa-deploy-promotion') {
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
            trigger("docker-jenkins/qa-deploy") {
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

  triggers {
    upstream('docker-jenkins/deploy', 'SUCCESS')
  }
}

job('docker-jenkins/qa-deploy') {
  steps {
    shell 'echo "QA deploy"'
  }
}

deliveryPipelineView('docker-jenkins/docker-jenkins-view') {
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
    component('docker-jenkins', 'docker-jenkins/build')
  }
}
