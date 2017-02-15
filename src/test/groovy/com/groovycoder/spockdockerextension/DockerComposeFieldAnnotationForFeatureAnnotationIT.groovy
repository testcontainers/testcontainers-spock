package com.groovycoder.spockdockerextension

import spock.lang.Shared
import spock.lang.Specification

class DockerComposeFieldAnnotationForFeatureAnnotationIT extends Specification {

    @Shared
    @DockerComposeServiceHost(service = "whoami", port = 80)
    String sharedWhoamiHost

    @Shared
    @DockerComposeServicePort(service = "whoami", port = 80)
    Integer sharedWhoamiPort

    @DockerComposeServiceHost(service = "whoami", port = 80)
    String whoamiHost

    @DockerComposeServicePort(service = "whoami", port = 80)
    Integer whoamiPort

    @DockerCompose(composeFile = "src/test/resources/docker-compose.yml", exposedServicePorts =
            [
                    @Expose(service = "whoami", port = 80)
            ])
    def "shared host is injected"() {
        expect:
        sharedWhoamiHost != null
    }

    @DockerCompose(composeFile = "src/test/resources/docker-compose.yml", exposedServicePorts =
            [
                    @Expose(service = "whoami", port = 80)
            ])
    def "shared port is injected"() {
        expect:
        sharedWhoamiPort != null
    }

    @DockerCompose(composeFile = "src/test/resources/docker-compose.yml", exposedServicePorts =
            [
                    @Expose(service = "whoami", port = 80)
            ])
    def "host is injected"() {
        expect:
        whoamiHost != null
    }

    @DockerCompose(composeFile = "src/test/resources/docker-compose.yml", exposedServicePorts =
            [
                    @Expose(service = "whoami", port = 80)
            ])
    def "port is injected"() {
        expect:
        whoamiPort != null
    }

    def "host is not injected"() {
        expect:
        sharedWhoamiHost == null
    }

    def "port is not injected"() {
        expect:
        sharedWhoamiPort == null
    }

}
