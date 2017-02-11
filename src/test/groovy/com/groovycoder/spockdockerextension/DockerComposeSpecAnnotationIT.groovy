package com.groovycoder.spockdockerextension

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import spock.lang.Specification

@DockerCompose("src/test/resources/docker-compose.yml")
class DockerComposeSpecAnnotationIT extends Specification {

    def "running compose defined container is accessible on configured port"() {
        given: "a http client"
        def client = HttpClientBuilder.create().build()

        when: "accessing web server"
        def response = client.execute(new HttpGet("http://localhost:8080"))

        then: "docker container is running and returns http status code 200"
        response.statusLine.statusCode == 200
    }

    def "compose container is accessible for all features"() {
        given: "a http client"
        def client = HttpClientBuilder.create().build()

        when: "accessing web server"
        def response = client.execute(new HttpGet("http://localhost:8080"))

        then: "docker container is running and returns http status code 200"
        response.statusLine.statusCode == 200
    }

}
