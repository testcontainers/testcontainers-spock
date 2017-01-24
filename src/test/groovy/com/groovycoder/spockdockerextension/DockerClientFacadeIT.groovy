package com.groovycoder.spockdockerextension

import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.conn.HttpHostConnectException
import org.apache.http.impl.client.CloseableHttpClient
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
        dockerClientFacade.rm()
    }

    def "running container is accessible on configured port"() {
        given: "a http client"
        def client = HttpClientBuilder.create().build()

        when: "running the container"
        dockerClientFacade.run()

        and: "accessing web server"
        def response = testHttpRequest(client)

        then: "docker container is running and returns http status code 200"
        response.statusLine.statusCode == 200
    }

    def "ip of container is accessible"() {
        given: "running container"
        dockerClientFacade.run()

        expect:
        dockerClientFacade.getIp().matches('^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\$')
    }

    def "should remove running docker container"() {
        given: "a http client"
        def client = HttpClientBuilder.create().build()

        and: "a running container"
        dockerClientFacade.run()

        when: "removing the container"
        dockerClientFacade.rm()

        and: "accessing web server"
        testHttpRequest(client)

        then: "container is not listening on port"
        thrown HttpHostConnectException
    }

    def "can stop container and start it again afterwards"() {
        given: "a http client"
        def client = HttpClientBuilder.create().build()

        and: "a started container"
        dockerClientFacade.run()

        when: "stopping the container"
        dockerClientFacade.stop()

        and: "accessing web server"
        testHttpRequest(client)

        then: "container is not listening on port"
        thrown HttpHostConnectException

        when: "starting the container again"
        dockerClientFacade.start()

        and: "accesing web server"
        def response = testHttpRequest(client)

        then: "docker container is running and returns http status code 200"
        response.statusLine.statusCode == 200
    }

    def "throws exception if trying to start container and port is already used"() {
        given: "a running container"
        dockerClientFacade.run()

        and: "a docker definition with a different image on the same port on which the other container is already running"
        Docker config = Stub(Docker)
        config.image() >> "emilevauge/whoami"
        config.ports() >> ["8080:80"]
        DockerClientFacade secondClient = new DockerClientFacade(config)

        when: "running this container"
        secondClient.run()

        then:
        thrown DockerRunException
    }

    private static CloseableHttpResponse testHttpRequest(CloseableHttpClient client) {
        client.execute(new HttpGet("http://localhost:8080"))
    }

}
