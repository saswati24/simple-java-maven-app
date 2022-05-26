# simple-java-maven-app

This repository is for the
[Build a Java app with Maven](https://jenkins.io/doc/tutorials/build-a-java-app-with-maven/)
tutorial in the [Jenkins User Documentation](https://jenkins.io/doc/).

The repository contains a simple Java application which outputs the string
"Hello world!" and is accompanied by a couple of unit tests to check that the
main application works as expected. The results of these tests are saved to a
JUnit XML report.

The `jenkins` directory contains an example of the `Jenkinsfile` (i.e. Pipeline)
you'll be creating yourself during the tutorial and the `scripts` subdirectory
contains a shell script with commands that are executed when Jenkins processes
the "Deliver" stage of your Pipeline.


```groovy
pipeline {
    agent any
    tools { 
        maven 'MAVEN_HOME' 
        jdk 'JAVA_HOME' 
    }
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "M2_HOME = ${M2_HOME}"
                ''' 
                git branch: 'master',
                credentialsId: 'github-cred',
                url: 'https://github.com/saswati24/simple-java-maven-app.git'

                sh "ls -lat"
                sh "mvn clean package"
                sh "java -jar target/my-app-1.0-SNAPSHOT-jar-with-dependencies.jar"
                }
        }
        stage('test') {
            steps {
                echo 'testing'
            }
        }
        stage('build') {
            steps {
                echo 'building'
            }
        }
        stage('deploy') {
            steps {
                echo 'deploying'
            }
        }    
        
    }
}
```
