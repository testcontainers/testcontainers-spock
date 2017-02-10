package com.groovycoder.spockdockerextension

import org.testcontainers.containers.DockerComposeContainer

class DockerComposeFacade {

    String composeFile
    DockerComposeContainer dockerComposeContainer

    DockerComposeFacade(String composeFile) {
        this.composeFile = composeFile
    }

    void up() {
        dockerComposeContainer = new DockerComposeContainer(new File(composeFile))
                .withLocalCompose(true)
        dockerComposeContainer.starting(null)
    }

    void down() {
        dockerComposeContainer.finished(null)
    }
}
