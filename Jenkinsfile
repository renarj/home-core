node {
    git url: 'git@github.com:renarj/robo-sdk.git'
    def mvnHome = tool 'M3'

    stage 'checkout'
    checkout scm

    stage 'build'
    sh "${mvnHome}/bin/mvn -B clean install"

    stage 'command-svc-docker'
    sh "${mvnHome}/bin/mvn -f command-svc/pom.xml -B clean package docker:build"

    stage 'edge-svc'
    sh "${mvnHome}/bin/mvn -f edge-svc/pom.xml -B clean package docker:build"

    stage 'state-svc'
    sh "${mvnHome}/bin/mvn -f state-svc/pom.xml -B clean package docker:build"

    stage 'archive'
    step([$class: 'ArtifactArchiver', artifacts: '**/target/*.jar', fingerprint: true])
}
