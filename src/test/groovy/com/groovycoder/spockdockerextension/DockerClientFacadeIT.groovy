package com.groovycoder.spockdockerextension

import org.apache.http.client.methods.HttpGet
import org.apache.http.conn.HttpHostConnectException
import org.apache.http.impl.client.HttpClientBuilder
import spock.lang.Shared
import spock.lang.Specification

class DockerClientFacadeIT extends Specification {

    @Shared
    DockerClientFacade dockerClientFacade

    def setup() {
        Docker config = Stub(Docker)
        config.image() >> "emilevauge/whoami"
        config.ports() >> ["8080:80"]
        dockerClientFacade = new DockerClientFacade(config)
    }

    def cleanup() {
        dockerClientFacade.stopContainer()
    }

    def "started container is accessible on configured port"() {
        given: "a http client"
        def client = HttpClientBuilder.create().build()

        when: "starting the container"
        dockerClientFacade.startContainer()

        and: "accessing web server"
        def response = client.execute(new HttpGet("http://localhost:8080"))

        then: "docker container is running and returns http status code 200"
        response.statusLine.statusCode == 200
    }

    def "ip of container is accessible"() {
        given: "started container"
        dockerClientFacade.startContainer()

        expect:
        dockerClientFacade.getContainerIp().matches('^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\$')
    }

    def "should stop running docker container"() {
        given: "a http client"
        def client = HttpClientBuilder.create().build()

        and: "a started container"
        dockerClientFacade.startContainer()

        when: "stopping the container"
        dockerClientFacade.stopContainer()

        and: "accessing web server"
        client.execute(new HttpGet("http://localhost:8080"))

        then: "container is not listening on port"
        thrown HttpHostConnectException
    }

}
