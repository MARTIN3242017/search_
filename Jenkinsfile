

pipeline {
   agent any
   tools {
      maven "maven3.6.3"
   }

   stages {
      stage('Build') {
         steps {
            // Get some code from a GitHub repository
            git 'git@github.com:MARTIN3242017/search.git'


            // Run Maven on a Unix agent.
            sh "mvn -Dmaven.test.failure.ignore=true clean package"


            // To run Maven on a Windows agent, use
            // bat "mvn -Dmaven.test.failure.ignore=true clean package"
         }


         post {
            // If Maven was able to run the tests, even if some of the test
            // failed, record the test results and archive the jar file.
            success {
               junit '**/target/surefire-reports/TEST-*.xml'
               archiveArtifacts 'target/*.jar'
            }
         }
      }
   }
}
