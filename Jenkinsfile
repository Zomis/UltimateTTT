#!/usr/bin/env groovy

@Library('ZomisJenkins')
import net.zomis.jenkins.Duga

pipeline {
    agent any

    stages {
        stage('Prepare') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh './gradlew clean test install --stacktrace --debug'
            }
        }
/*
        stage('Results') {
            steps {
                withSonarQubeEnv('docker-sonar') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
*/
        stage('Release check') {
            steps {
                sh './gradlew uploadArchives'
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: '**/build/test-results/junit-platform/TEST-*.xml'
        }
        success {
            zpost(0)
        }
        unstable {
            zpost(1)
        }
        failure {
            zpost(2)
        }
    }
}
