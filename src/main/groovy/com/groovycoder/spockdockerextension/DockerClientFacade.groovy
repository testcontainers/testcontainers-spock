package com.groovycoder.spockdockerextension

import de.gesellix.docker.client.DockerClient
import de.gesellix.docker.client.DockerClientImpl

class DockerClientFacade {

    DockerClient dockerClient
    String image
    String name
    Map clientSpecificContainerConfig
    def containerHandle

    DockerClientFacade(Docker containerConfig) {
        image = containerConfig.image()
        name = containerConfig.name()
        dockerClient = new DockerClientImpl()
        clientSpecificContainerConfig = new DockerContainerConfigBuilder(containerConfig).build()
    }

    void run() {
        containerHandle = dockerClient.run(image, clientSpecificContainerConfig, "latest")

        if (!status.success) {
            throw new DockerRunException("Error running the container: $status.text")
        }

        containerHandle
    }

    void rm() {
        dockerClient.stop(id)
        dockerClient.rm(id)
    }

    void start() {
        dockerClient.startContainer(id)
    }

    void stop() {
        dockerClient.stop(id)
    }

    String getIp() {
        def containerInspection = dockerClient.inspectContainer(id)
        return containerInspection.content.NetworkSettings.Networks.bridge.IPAddress
    }

    private String getId() {
        containerHandle.container.content.Id
    }

    private getStatus() {
        containerHandle.status.status
    }
}
