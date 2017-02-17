package com.groovycoder.spockdockerextension.docker

import org.apache.http.client.methods.HttpGet
import org.apache.http.conn.HttpHostConnectException
import org.apache.http.impl.client.HttpClientBuilder
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
class DockerComposeFacadeIT extends Specification {

    @Shared
    DockerComposeFacade dockerCompose

    def "running compose defined container is accessible on configured port"() {
        given: "a docker compose facade"
        dockerCompose = new DockerComposeFacade("src/test/resources/docker-compose.yml")

        and: "a http client"
        def client = HttpClientBuilder.create().build()

        when: "running the container"
        dockerCompose.up()

        and: "accessing web server"
        def response = client.execute(new HttpGet("http://localhost:8080"))

        then: "docker container is running and returns http status code 200"
        response.statusLine.statusCode == 200
    }

    def "shuts down running compose container"() {
        given: "a http client"
        def client = HttpClientBuilder.create().build()

        when: "shutting down the container"
        dockerCompose.down()

        and: "accessing web server"
        client.execute(new HttpGet("http://localhost:8080"))

        then: "container is not listening on port"
        thrown HttpHostConnectException
    }

}
