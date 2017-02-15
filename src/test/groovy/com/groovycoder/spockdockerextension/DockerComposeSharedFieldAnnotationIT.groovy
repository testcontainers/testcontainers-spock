package com.groovycoder.spockdockerextension

import spock.lang.Shared
import spock.lang.Specification

@DockerCompose(composeFile = "src/test/resources/docker-compose.yml", exposedServicePorts =
        [
                @Expose(service = "whoami", port = 80)
        ], shared = true)
class DockerComposeSharedFieldAnnotationIT extends Specification {

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

    def "shared host is injected"() {
        expect:
        sharedWhoamiHost != null
    }

    def "shared port is injected"() {
        expect:
        sharedWhoamiPort != null
    }

    def "instance host is not injected"() {
        expect:
        whoamiHost == null
    }

    def "instance port is not injected"() {
        expect:
        whoamiPort == null
    }
}
