package com.groovycoder.spockdockerextension

import spock.lang.Specification
import spock.lang.Unroll

class DockerContainerConfigBuilderTest extends Specification {

    @Unroll
    def "parsed port binding into client specific format for binding #binding"() {
        given: "a config return the port binding"
        Docker config = Stub(Docker)
        config.ports() >> binding

        and: "the config builder"
        def builder = new DockerContainerConfigBuilder(config)

        expect:
        builder.extractPortBindings() == expectedResult

        where:
        binding       || expectedResult
        ["4712:4711"] || ["ExposedPorts": ["4711/tcp": [:]], "HostConfig": ["PortBindings": ["4711/tcp": [["HostIp": "0.0.0.0", "HostPort": "4712"]]]]]
        ["8080:80"]   || ["ExposedPorts": ["80/tcp": [:]], "HostConfig": ["PortBindings": ["80/tcp": [["HostIp": "0.0.0.0", "HostPort": "8080"]]]]]
    }

}
