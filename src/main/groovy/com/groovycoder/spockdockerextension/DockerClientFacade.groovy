package com.groovycoder.spockdockerextension

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.exception.InternalServerErrorException
import com.github.dockerjava.api.exception.NotFoundException
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientBuilder
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.core.command.PullImageResultCallback
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DockerClientFacade {

    private final Docker imageConfig
    private final DockerClient dockerClient

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerClientFacade)

    private String id

    DockerClientFacade(Docker containerConfig) {
        this.imageConfig = containerConfig
        dockerClient = initializeClient()
    }

    private static DockerClient initializeClient() {
        def clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
        return DockerClientBuilder.getInstance(clientConfig).build()
    }

    void startContainer() {
        def portBindings = parsePortBindings()
        ensureImageExists()
        id = createContainer(portBindings)
        dockerClient.startContainerCmd(id).exec()
    }

    private String createContainer(List<PortBinding> portBindings) {
        dockerClient.createContainerCmd(imageConfig.image())
                .withPortBindings(portBindings)
                .exec().id
    }

    private void ensureImageExists() {
        try {
            dockerClient.inspectImageCmd(imageConfig.image()).exec()
        } catch (NotFoundException e) {
            dockerClient.pullImageCmd(imageConfig.image()).exec(new PullImageResultCallback()).awaitSuccess()
        }
    }

    void stopContainer() {
        dockerClient.stopContainerCmd(id).exec()
        try {
            dockerClient.removeContainerCmd(id).exec()
        } catch(InternalServerErrorException e) {
            LOGGER.error("error while removing the container", e)
        }
    }

    List<PortBinding> parsePortBindings() {
        def ports = imageConfig.ports().first()

        return [parsePortBinding(ports)]
    }

    private PortBinding parsePortBinding(com.groovycoder.spockdockerextension.PortBinding ports) {
        def parsedBinding = PortBinding.parse(ports.value())
        return parsedBinding
    }
}
