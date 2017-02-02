package com.groovycoder.spockdockerextension

import org.testcontainers.containers.ContainerLaunchException
import org.testcontainers.containers.GenericContainer

import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

class DockerClientFacade {

    GenericContainer dockerClient
    String image
    String name

    DockerClientFacade(Docker containerConfig) {
        image = containerConfig.image()
        name = containerConfig.name()
        def latestImage = "$image:latest"

        Future<String> futureImage = CompletableFuture.completedFuture(latestImage.toString())
        dockerClient = new GenericContainer(futureImage)
        dockerClient.setPortBindings(containerConfig.ports().toList())
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
