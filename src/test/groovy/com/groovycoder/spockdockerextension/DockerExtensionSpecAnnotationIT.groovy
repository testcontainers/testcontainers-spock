package com.groovycoder.spockdockerextension

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
@Docker(image = "emilevauge/whoami", ports = ["8080:80", "8081:80"])
class DockerExtensionSpecAnnotationIT extends Specification {

    @Shared
    DockerClientFacade dockerClientFacade

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

    def "should be able to access to docker container via its ip"() {
        given: "a http client"
        def client = HttpClientBuilder.create().build()

        when: "retrieving the ip of the container"
        def ip = dockerClientFacade.ip

        and: "accessing web server"
        def response1 = client.execute(new HttpGet("http://$ip:80"))

        then: "docker container is running and returns http status code 200"
        response1.statusLine.statusCode == 200

    }

}
