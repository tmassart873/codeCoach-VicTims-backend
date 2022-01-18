pipeline {
    agent any

    tools {
        jdk 'jdk-17'
        maven 'Maven'

    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean test-compile'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn -Dmaven.test.failure.ignore=true test'
            }
        }
    }

    post {
        always {
            publishCoverage adapters: [jacocoAdapter('**/target/site/jacoco/jacoco.xml')]
            junit '**/target/surefire-reports/*.xml'
        }
    }
}