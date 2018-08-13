node {
    stage ('checkout') {
        git url: 'git@github.com:renarj/home-core.git'
    }

    stage ('build') {
        withMaven(maven: 'M3', jdk: 'JDK10') {
            sh "mvn clean install"
        }
    }

    stage ('command-svc-docker') {
        withMaven(maven: 'M3', jdk: 'JDK10') {
            sh "mvn -f command-svc/pom.xml -B clean package docker:build"
        }
    }


    stage ('edge-svc-docker') {
        withMaven(maven: 'M3', jdk: 'JDK10') {
            sh "mvn -f edge-svc/pom.xml -B clean package docker:build"
        }
    }

    stage ('state-svc-docker') {
        withMaven(maven: 'M3', jdk: 'JDK10') {
            sh "mvn -f state-svc/pom.xml -B clean package docker:build"
        }
    }

    stage ('Publish containers') {
        shouldPublish = input message: 'Publish Containers?', parameters: [[$class: 'ChoiceParameterDefinition', choices: 'yes\nno', description: '', name: 'Deploy']]
        if(shouldPublish == "yes") {
         echo "Publishing docker containers"
         sh "\$(aws ecr get-login)"

         sh "docker tag home-core/state-svc:latest 474345402034.dkr.ecr.eu-west-1.amazonaws.com/state-svc:latest"
         sh "docker push ${accountId}.dkr.ecr.eu-west-1.amazonaws.com/state-svc:latest"

         sh "docker tag home-core/command-svc:latest 474345402034.dkr.ecr.eu-west-1.amazonaws.com/command-svc:latest"
         sh "docker push ${accountId}.dkr.ecr.eu-west-1.amazonaws.com/command-svc:latest"

         sh "docker tag home-core/edge-svc:latest 474345402034.dkr.ecr.eu-west-1.amazonaws.com/edge-svc:latest"
         sh "docker push ${accountId}.dkr.ecr.eu-west-1.amazonaws.com/edge-svc:latest"
        }
    }

    stage 'archive'
    step([$class: 'ArtifactArchiver', artifacts: '**/target/*.jar', fingerprint: true])
}