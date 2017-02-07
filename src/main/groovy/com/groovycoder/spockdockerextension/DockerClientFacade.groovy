package com.groovycoder.spockdockerextension

import org.testcontainers.containers.ContainerLaunchException
import org.testcontainers.containers.FixedHostPortGenericContainer

class DockerClientFacade {

    FixedHostPortGenericContainer dockerClient
    Docker config
    String name

    DockerClientFacade(Docker containerConfig) {
        name = containerConfig.name()
        this.config = containerConfig
    }

    void run() {
        try {
            start()
        } catch (ContainerLaunchException e) {
            throw new DockerRunException("Error running the container", e)

        }
    }

    void rm() {
        dockerClient.stop()
    }

    void start() {
        String image = config.image()
        String imageWithTag = "$image:latest"
        dockerClient = new FixedHostPortGenericContainer(imageWithTag)
        config.ports().each { String portMapping ->
            def split = portMapping.split(":")
            dockerClient.withFixedExposedPort(split[0].toInteger(), split[1].toInteger())
        }
        dockerClient.start()
    }

    void stop() {
        dockerClient.stop()
    }

    String getIp() {
        //noinspection GrDeprecatedAPIUsage
        dockerClient.containerInfo.networkSettings.ipAddress
    }

}
