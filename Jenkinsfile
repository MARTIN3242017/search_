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
               //文件归档(后续可手动下载) 
               archiveArtifacts 'target/*.jar'
               sh "echo 'hello world!!!'"
               sh "ls"
               sh "pwd"
               sh "cp target/search-0.0.1-SNAPSHOT.jar /mydata/jenkins_home/search.jar"
               sh "cd /mydata/jenkins_home"
               sh "ls"
               sh "pwd"
               //sh "java -jar target/search-0.0.1-SNAPSHOT.jar"
            }
         }
      }
   }
}
