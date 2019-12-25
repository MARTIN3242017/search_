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
            sh "mvn -Dmaven.test.failure.ignore=true clean package"
         }
         post {
            success {
               archiveArtifacts 'target/*.jar'
               sh "echo 'hello world!!!'"
               sh "ls"
               sh "pwd"
               sh "cd target"
               sh "ls"
               sh "pwd"
               sh "cp ../Dockerfile ./"
               sh "ls"
               sh "docker build -t index ."
               sh "docker run -d -p 9090:9090 search"
            }
         }
      }
   }
}
