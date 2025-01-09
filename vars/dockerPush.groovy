def call(List imageList, timeoutMinutes=30) {
  label 'Docker Push'
  List images = []
  images = imageList

  timeout (time: timeoutMinutes, unit: 'MINUTES') {
    for (image in images) {
      withEnv (["IMAGE=$image"]) {
        echo "Docker Push '$IMAGE'"
        sh(
          label: 'Docker Push',
          script: "docker push '$IMAGE'"
        )
      }
    }
  }
}
