pipeline {
  agent any
  stages {
    stage('step01') {
      post {
        always {
          archiveArtifacts(artifacts: 'target/*.jar', fingerprint: true)
        }

      }
      steps {
        sleep 1
        sh 'mvn -B -search clean package'
        sh 'sh \'echo "Hello world"\''
      }
    }

  }
}