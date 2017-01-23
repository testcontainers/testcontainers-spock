package com.groovycoder.spockdockerextension

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import spock.lang.Shared
import spock.lang.Specification


@DockerContainers(
        [
                @Docker(image = "emilevauge/whoami", ports = ["8000:80"]),
                @Docker(image = "emilevauge/whoami", ports = ["9000:80"])
        ]
)
class DockerContainersExtensionSpecAnnotationIT extends Specification {

    @Shared
    Set<DockerClientFacade> dockerClientFacades = [] as Set

    @Shared
    Set genericSet = [] as Set


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

    def "container handles are injected into spec"() {
        expect:
        dockerClientFacades.size() == 2
    }

    def "container handles are not injected into other collections than set"() {
        expect:
        genericSet.empty
    }

}
