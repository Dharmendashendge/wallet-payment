pipeline {
  agent any

  tools {
    jdk   'Temurin-17'      // <-- rename to your configured JDK tool name
    maven 'Maven-3.9.x'     // <-- rename to your configured Maven tool name
  }

  options {
    timestamps()
    ansiColor('xterm')
    skipStagesAfterUnstable()
  }

  environment {
    // Set your SonarQube project key (create in SonarQube UI if needed)
    SONAR_PROJECT_KEY = 'wallet-payment'
    // Optional: set a readable display name in Sonar
    SONAR_PROJECT_NAME = 'Wallet Payment'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build & Unit Test') {
      steps {
        // Use profile "coverage" (defined in the POM snippet below) to generate JaCoCo XML
        sh 'mvn -B -Pcoverage clean verify'
      }
      post {
        always {
          // Publish Surefire/JUnit results to Jenkins Test Results (trend graph etc.)
          junit '**/target/surefire-reports/*.xml'  // standard Maven Surefire output path
          archiveArtifacts artifacts: '**/target/*.jar', onlyIfSuccessful: true
        }
      }
    }

    stage('Static Analysis (SonarQube)') {
      steps {
        // Inject SONAR_HOST_URL and SONAR_AUTH_TOKEN from Jenkins global SonarQube server config
        withSonarQubeEnv('MySonar') {               // <-- rename to your SonarQube server name
          sh """
            mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
              -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
              -Dsonar.projectName='${SONAR_PROJECT_NAME}'
          """
        }
      }
    }

    stage('Quality Gate') {
      steps {
        // Wait (up to 1h) for Sonar to compute Quality Gate and fail fast if it fails
        timeout(time: 1, unit: 'HOURS') {
          waitForQualityGate abortPipeline: true
        }
      }
    }
  }
}
