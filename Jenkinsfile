pipeline {
  agent any
  stages {
    stage('step01') {
      steps {
        sleep 1
        sh 'mvn -B -search clean package'
        sh 'java -jar search.jar'
      }
       post {
             always {
                 archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
             }
        }
    }

  }
}
