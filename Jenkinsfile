@Library('github.com/mtrojanowski/example-company@master') _

pipeline {
  environment {
    DOCKER_REGISTRY='company_account'
    DOCKER_IMAGE="${DOCKER_REGISTRY}/testing-image"
    DOCKER_TAG = "$GIT_COMMIT"
  }
  agent {
    kubernetes {
      yaml '''
        apiVersion: v1
        kind: Pod
        spec:
          containers:
          - name: docker
            image: myreg/docker:1
            volumeMounts:
            - name: build-cache
              mountPath: /var/lib/docker
              subPath: docker
            command:
            - cat
            tty: true
            securityContext:
              privileged: true
			  
          - name: docker
            image: docker:latest
            command:
            - cat
            tty: true
            volumeMounts:
             - mountPath: /var/run/docker.sock
               name: docker-sock
          volumes:
          - name: docker-sock
            hostPath:
              path: /var/run/docker.sock    
        '''
    }
  }
  stages {
    stage('Build-Docker-Image') {
      steps {
        container('docker') {
          // kiedy wybierzesz pierwszy docker container jako rozwiazanie.
          // sh 'dockerd & > /dev/null'
          // sleep(time: 10, unit: "SECONDS")
          sh 'docker build -t '$DOCKER_IMAGE':'$DOCKER_TAG' .'
		  
        }
      }
    }
    stage('Login-Into-Docker') {
      steps {
        withCredentials([string(credentialsId: 'docker-access-token', variable: 'password')]) {
          container('docker') {
            sh 'docker login -u ${DOCKER_REGISTRY} -p ${password}'
          }
        }
      }
    }
    stage('Push-Images-Docker-to-DockerHub') {
      steps {
        container('docker') {
          dockerPush('$DOCKER_IMAGE':'$DOCKER_TAG')
        }
      }
    }
  }
  post {
    always {
      container('docker') {
        sh 'docker logout'
      }
    }
  }
}
