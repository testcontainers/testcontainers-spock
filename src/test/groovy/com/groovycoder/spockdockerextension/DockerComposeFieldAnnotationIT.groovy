package com.groovycoder.spockdockerextension

import spock.lang.Shared
import spock.lang.Specification

@DockerCompose(composeFile = "src/test/resources/docker-compose.yml", exposedServicePorts =
        [
                @Expose(service = "whoami", port = 80)
        ], shared = false)
class DockerComposeFieldAnnotationIT extends Specification {

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

    def "shared host is not injected"() {
        expect:
        sharedWhoamiHost == null
    }

    def "shared port is not injected"() {
        expect:
        sharedWhoamiPort == null
    }

    def "instance host is injected"() {
        expect:
        whoamiHost != null
    }

    def "instance port is injected"() {
        expect:
        whoamiPort != null
    }
}
