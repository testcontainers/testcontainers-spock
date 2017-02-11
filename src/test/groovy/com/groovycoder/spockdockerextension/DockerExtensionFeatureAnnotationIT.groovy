package com.groovycoder.spockdockerextension

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import spock.lang.Ignore
import spock.lang.Specification

class DockerExtensionFeatureAnnotationIT extends Specification {

    @Docker(image = "emilevauge/whoami", ports = ["8080:80"])
    def "should start accessible docker container"() {
        given: "a http client"
        def client = HttpClientBuilder.create().build()

        when: "accessing web server"
        def response1 = client.execute(new HttpGet("http://localhost:8080"))

        then: "docker container is running and returns http status code 200"
        response1.statusLine.statusCode == 200
    }

    @DockerContainers(
            [
                    @Docker(image = "emilevauge/whoami", ports = ["8000:80"]),
                    @Docker(image = "emilevauge/whoami", ports = ["9000:80"])
            ]
    )
    def "should start multiple containers"() {
        given: "a http client"
        def client = HttpClientBuilder.create().build()

        when: "accessing web server"
        def response1 = client.execute(new HttpGet("http://localhost:9000"))
        def response2 = client.execute(new HttpGet("http://localhost:8000"))

        then: "docker container is running and returns http status code 200"
        response1.statusLine.statusCode == 200
        response2.statusLine.statusCode == 200
    }


    @Ignore
    @Docker(image = "foobarkivisaurus", ports = [])
    def "fails if container image does not exist"() {
        expect:
        false
    }

    @Ignore
    @Docker(image = "emilevauge/whoami", ports = ["9090:80"], name = "feature")
    def "fails if port is already bound"() {
        expect:
        false
    }
}
