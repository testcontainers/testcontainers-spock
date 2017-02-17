package com.groovycoder.spockdockerextension.docker

import com.groovycoder.spockdockerextension.Docker
import com.groovycoder.spockdockerextension.DockerRunException
import org.testcontainers.containers.ContainerLaunchException
import org.testcontainers.containers.FixedHostPortGenericContainer

class DockerClientFacade {

    FixedHostPortGenericContainer dockerClient
    final String name
    final String[] ports
    final String image

    DockerClientFacade(Docker containerConfig) {
        name = containerConfig.name()
        ports = containerConfig.ports()
        this.image = concatImageWithDefaultTagIfNeeded(containerConfig.image())
    }

    private static String concatImageWithDefaultTagIfNeeded(String image) {
        (image.contains(":")) ? image : image + ":latest"
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
        dockerClient = new FixedHostPortGenericContainer(image)
        ports.each { String portMapping ->
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
