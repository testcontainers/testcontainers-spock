package com.groovycoder.spockdockerextension.docker

import com.groovycoder.spockdockerextension.Docker
import com.groovycoder.spockdockerextension.DockerRunException
import com.groovycoder.spockdockerextension.Env
import org.testcontainers.containers.ContainerLaunchException
import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.containers.wait.WaitStrategy

class DockerClientFacade {

    FixedHostPortGenericContainer dockerClient
    final String name
    final String[] ports
    final String image
    final Env[] env
    WaitStrategy waitStrategy

    DockerClientFacade(Docker containerConfig) {
        name = containerConfig.name()
        ports = containerConfig.ports()
        env = containerConfig.env()
        this.image = concatImageWithDefaultTagIfNeeded(containerConfig.image())
        waitStrategy = ((Closure) containerConfig.waitStrategy().newInstance(this, this))()
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

        env.each { Env e ->
            dockerClient.withEnv(e.key(), e.value())
        }

        dockerClient.waitingFor(waitStrategy)

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
