package com.groovycoder.spockdockerextension

import org.apache.http.client.methods.HttpGet
import org.apache.http.conn.HttpHostConnectException
import org.apache.http.impl.client.HttpClientBuilder
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
class DockerClientFacadeIT extends Specification {

    @Shared
    DockerClientFacade dockerClientFacade

    def "should start specified docker container"() {
        given: "a docker container config"
        Docker config = Stub(Docker)
        config.image() >> "emilevauge/whoami"
        config.ports() >> ["8080:80"]

        and: "the client facade"
        dockerClientFacade = new DockerClientFacade(config)

        and: "a http client"
        def client = HttpClientBuilder.create().build()

        when: "starting the container"
        dockerClientFacade.startContainer()

        and: "accessing web server"
        def response = client.execute(new HttpGet("http://localhost:8080"))

        then: "docker container is running and returns http status code 200"
        response.statusLine.statusCode == 200
    }

    def "should stop running docker container"() {
        given: "a http client"
        def client = HttpClientBuilder.create().build()

        when: "stopping the container"
        dockerClientFacade.stopContainer()

        and: "accessing web server"
        client.execute(new HttpGet("http://localhost:8080"))

        then: "container is not listening on port"
        thrown HttpHostConnectException
    }

}
