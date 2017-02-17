package com.groovycoder.spockdockerextension.docker

import com.groovycoder.spockdockerextension.ExposedServiceInstance
import org.testcontainers.containers.DockerComposeContainer

class DockerComposeFacade {

    String composeFile
    DockerComposeContainer dockerComposeContainer
    Collection<ExposedServiceInstance> exposedServiceInstances

    DockerComposeFacade(String composeFile) {
        this(composeFile, Collections.emptyList())
    }

    DockerComposeFacade(String composeFile, Collection<ExposedServiceInstance> exposedServiceInstances) {
        this.composeFile = composeFile
        this.exposedServiceInstances = exposedServiceInstances.asImmutable()
    }

    void up() {
        dockerComposeContainer = new DockerComposeContainer(new File(composeFile))
        exposedServiceInstances.each {
            dockerComposeContainer.withExposedService(it.service, it.instance, it.port)
        }
        dockerComposeContainer.starting(null)
    }

    void down() {
        dockerComposeContainer.finished(null)
    }

    def getServiceHost(String serviceName, int port) {
        if (!serviceName.matches(".*_[0-9]+")) {
            return getServiceHost(serviceName, port, 1)
        }
        return dockerComposeContainer.getServiceHost(serviceName, port)
    }

    def getServiceHost(String serviceName, int port, int instance) {
        if (!serviceName.matches(".*_[0-9]+")) {
            serviceName += "_$instance"
        }
        return dockerComposeContainer.getServiceHost(serviceName, port)
    }

    def getServicePort(String serviceName, int port) {
        if (!serviceName.matches(".*_[0-9]+")) {
            return getServicePort(serviceName, port, 1)
        }
        return dockerComposeContainer.getServiceHost(serviceName, port)
    }

    def getServicePort(String serviceName, int port, int instance) {
        if (!serviceName.matches(".*_[0-9]+")) {
            serviceName += "_$instance"
        }
        return dockerComposeContainer.getServicePort(serviceName, port)
    }
}
