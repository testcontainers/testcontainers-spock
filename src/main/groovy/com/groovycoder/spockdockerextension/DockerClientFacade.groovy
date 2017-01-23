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

    void startContainer() {
        containerHandle = dockerClient.run(image, clientSpecificContainerConfig, "latest")
    }

    void stopContainer() {
        def id = containerId()

        dockerClient.stop(id)
        dockerClient.rm(id)
    }

    private String containerId() {
        containerHandle.container.content.Id
    }

    String getContainerIp() {
        def containerInspection = dockerClient.inspectContainer(containerId())
        return containerInspection.content.NetworkSettings.Networks.bridge.IPAddress
    }
}
