package com.groovycoder.spockdockerextension.docker

import spock.lang.Specification

class DockerClientFacadeTest extends Specification {

    def "defaults to latest image if no tag specified"() {
        given: "the config containing image without tag"
        def config = new DockerConfigBuilder(image: "imageName").build()

        when: "creating the client"
        def client = new DockerClientFacade(config)

        then: "tag is latest"
        client.image == "imageName:latest"
    }

    def "uses specified image tag if specified"() {
        given: "the config containing image with tag"
        def config = new DockerConfigBuilder(image: "imageName:123").build()

        when: "creating the client"
        def client = new DockerClientFacade(config)

        then: "tag is given tag"
        client.image == "imageName:123"
    }

}
