package com.groovycoder.spockdockerextension.docker

import com.groovycoder.spockdockerextension.Docker
import com.groovycoder.spockdockerextension.DockerRunException
import com.groovycoder.spockdockerextension.Env
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.conn.HttpHostConnectException
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.testcontainers.containers.wait.Wait
import spock.lang.Shared
import spock.lang.Specification

class DockerClientFacadeIT extends Specification {

    static final String WHOAMI_IMAGE = "emilevauge/whoami"
    static final String ENVINFO_IMAGE = "kiview/env-info"

    @Shared
    DockerClientFacade dockerClientFacade


    def cleanup() {
        dockerClientFacade.rm()
    }

    def "running container is accessible on configured port"() {
        given: "a basic facade"
        buildWhoamiFacade()

        and: "a http client"
        def client = HttpClientBuilder.create().build()

        when: "running the container"
        dockerClientFacade.run()

        and: "accessing web server"
        def response = testHttpRequest(client)

        then: "docker container is running and returns http status code 200"
        response.statusLine.statusCode == 200
    }

    def "ip of container is accessible"() {
        given: "a basic facade"
        buildWhoamiFacade()

        and: "running container"
        dockerClientFacade.run()

        expect:
        dockerClientFacade.getIp().matches('^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\$')
    }

    def "should remove running docker container"() {
        given: "a basic facade"
        buildWhoamiFacade()

        and: "a http client"
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
        given: "a basic facade"
        buildWhoamiFacade()

        and: "a http client"
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

    def "set environment variables inside container"() {
        given: "some environment variables on the container"
        Env fooEnv = Stub(Env)
        fooEnv.key() >> "foo"
        fooEnv.value() >> "bar"

        Env dockerEnv = Stub(Env)
        dockerEnv.key() >> "docker"
        dockerEnv.value() >> "gopher"

        Docker config = new DockerConfigBuilder(
                image: ENVINFO_IMAGE,
                ports: ["8080:8080"],
                env: [fooEnv, dockerEnv],
                waitStrategy: { Wait.forHttp("/env/foo") }
        ).build()

        dockerClientFacade = new DockerClientFacade(config)

        when: "running the container"
        dockerClientFacade.run()

        and: "accessing web server"
        def response = HttpClientBuilder.create().build().execute(new HttpGet("http://localhost:8080/env/foo"))
        def foo = response.entity.content.text

        and: "again for another value"
        response = HttpClientBuilder.create().build().execute(new HttpGet("http://localhost:8080/env/docker"))
        def docker = response.entity.content.text

        then: "response contains set environment variables"
        foo == "bar"
        docker == "gopher"
    }


    def "throws exception if trying to start container and port is already used"() {
        given: "a basic facade"
        buildWhoamiFacade()

        and: "a running container"
        dockerClientFacade.run()

        and: "a docker definition with a different image on the same port on which the other container is already running"
        DockerClientFacade secondClient = new DockerClientFacade(buildWhoamiConfig())

        when: "running this container"
        secondClient.run()

        then:
        thrown DockerRunException
    }

    private void buildWhoamiFacade() {
        Docker config = buildWhoamiConfig()
        dockerClientFacade = new DockerClientFacade(config)
    }

    private static Docker buildWhoamiConfig() {
        new DockerConfigBuilder(image: WHOAMI_IMAGE, ports: ["8080:80"]).build()
    }

    private static CloseableHttpResponse testHttpRequest(CloseableHttpClient client) {
        client.execute(new HttpGet("http://localhost:8080"))
    }

}
