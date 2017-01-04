package com.groovycoder.spockdockerextension

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import spock.lang.Specification

@Docker(image = "emilevauge/whoami", ports = ["8080:80"])
class DockerExtensionIT extends Specification {

    def "should start accessible docker container"() {
        given: "a http client"
        def client = HttpClientBuilder.create().build()

        when: "accessing web server"
        def response = client.execute(new HttpGet("http://localhost:8081"))

        then: "docker container is running and returns http status code 200"
        response.statusLine.statusCode == 200
    }

}
