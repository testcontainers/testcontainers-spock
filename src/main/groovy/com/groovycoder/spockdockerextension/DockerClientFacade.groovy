package com.groovycoder.spockdockerextension

import de.gesellix.docker.client.DockerClient
import de.gesellix.docker.client.DockerClientImpl

class DockerClientFacade {

    DockerClient dockerClient
    String image
    Map clientSpecificContainerConfig
    def containerStatus

    DockerClientFacade(Docker containerConfig) {
        image = containerConfig.image()
        dockerClient = new DockerClientImpl()
        clientSpecificContainerConfig = new DockerContainerConfigBuilder(containerConfig).build()
    }

    void startContainer() {
        containerStatus = dockerClient.run(image, clientSpecificContainerConfig, "latest")
    }

    void stopContainer() {
        def id = containerStatus.container.content.Id

        dockerClient.stop(id)
        dockerClient.rm(id)
    }

}
