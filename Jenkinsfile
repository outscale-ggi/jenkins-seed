pipeline {
        agent { label 'master' }
        stages{
            stage('Create Jobs') {
                steps {
                    jobDsl targets: '**/job.groovy',
                        removedJobAction: 'DELETE',
                        removedViewAction: 'DELETE',
                        removedConfigFilesAction: 'DELETE',
                        sandbox: false
                }
            }
       }
}

