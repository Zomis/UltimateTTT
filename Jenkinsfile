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
                sh 'mvn clean package'
            }
        }
        stage('Results') {
            steps {
                junit allowEmptyResults: true, testResults: '**/target/surefire-reports/TEST-*.xml'

                withSonarQubeEnv('docker-sonar') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
        stage('Release check') {
            steps {
                zreleaseMaven()
            }
        }
    }

    post {
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
