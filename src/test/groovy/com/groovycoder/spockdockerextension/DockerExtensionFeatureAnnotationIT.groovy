package com.groovycoder.spockdockerextension

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
class DockerExtensionFeatureAnnotationIT extends Specification {

    @Docker(image = "emilevauge/whoami", ports = ["8080:80", "8081:80"])
    def "should start accessible docker container"() {
        given: "a http client"
        def client = HttpClientBuilder.create().build()

        when: "accessing web server"
        def response1 = client.execute(new HttpGet("http://localhost:8080"))

        and: "accessing the web server on another port"
        def response2 = client.execute(new HttpGet("http://localhost:8081"))

        then: "docker container is running and returns http status code 200"
        response1.statusLine.statusCode == 200
        response2.statusLine.statusCode == 200
    }

    def "container should be offline if no container specified afterwards"() {
        given: "a http client"
        def client = HttpClientBuilder.create().build()

        when: "accessing web server"
        client.execute(new HttpGet("http://localhost:8080"))

        then: "server is not reachable"
        thrown IOException
    }

    @Docker(image = "emilevauge/whoami", ports = ["9000:80"])
    def "should start accessible docker container on another port"() {
        given: "a http client"
        def client = HttpClientBuilder.create().build()

        when: "accessing web server"
        def response1 = client.execute(new HttpGet("http://localhost:9000"))

        then: "docker container is running and returns http status code 200"
        response1.statusLine.statusCode == 200
    }





}
