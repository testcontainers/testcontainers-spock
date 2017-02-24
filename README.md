# TestContainers-Spock
[![Build Status](https://travis-ci.org/kiview/spock-docker-extension.svg?branch=travis)](https://travis-ci.org/kiview/spock-docker-extension)
[![](https://jitpack.io/v/kiview/spock-docker-extension.svg)](https://jitpack.io/#kiview/spock-docker-extension)

[Spock](https://github.com/spockframework/spock) extension for [TestContainers](https://github.com/testcontainers/testcontainers-java) library, which allows to use Docker containers inside of Spock tests.


# Usage

All annotations can be used on Feature as well as on Specification level.

## @Docker

You can use the annotation like this:

```groovy
@Docker(image = "nginx", ports = ["8080:80"])
class DockerExtensionIT extends Specification {
   // tests
}
```

You can also specify a field of type `DockerClientFacade` including a `@Shared` annotation which will be used automatically to inject the facade into the Specification.
Have a look into the included integration tests to see more examples.


## @DockerContainers

The `@DockerContainers` annotation can be used to define multiple containers.

```groovy
@DockerContainers(
        [
                @Docker(image = "emilevauge/whoami", ports = ["8000:80"], name = "first"),
                @Docker(image = "emilevauge/whoami", ports = ["9000:80"], name = "second")
        ]
)
class DockerContainersExtensionSpecAnnotationIT extends Specification {

    @Shared
    Map<String, DockerClientFacade> dockerClientFacades = [:]


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
}
```

## @DockerCompose

The `@DockerCompose` annotation can be used with regular `docker-compose.yml` files and should work even if docker-compose isn't installed locally.
Use the `shared` value of the annotation to define is the docker-compose environment should be shared between test runs. 

```groovy
 @DockerCompose(composeFile = "src/test/resources/docker-compose-uptime.yml", 
                exposedServicePorts = [@Expose(service = "whoami", port = 80)], shared = true)
 @Stepwise
 class DockerComposeSpecSharedAnnotationIT extends Specification {

 
     @Shared
     DockerComposeFacade sharedDockerComposeFacade
 
     DockerComposeFacade instanceDockerComposeFacade
 
     @Shared
     String lastHost
 
     def "running compose defined container is accessible on configured port"() {
         given: "a http client"
         def client = HttpClientBuilder.create().build()
 
         when: "accessing web server"
         def response = client.execute(new HttpGet("http://localhost:8080"))
 
         then: "docker container is running and returns http status code 200"
         response.statusLine.statusCode == 200
     }
}
```

You can also inject the internal ips and ports of the containers inside the docker network into you test specification.

```groovy 
 @DockerCompose(composeFile = "src/test/resources/docker-compose-uptime.yml", 
                exposedServicePorts = [@Expose(service = "whoami", port = 80)], shared = true)
class DockerComposeSharedFieldAnnotationIT extends Specification {

    @Shared
    @DockerComposeServiceHost(service = "whoami", port = 80)
    String sharedWhoamiHost

    @Shared
    @DockerComposeServicePort(service = "whoami", port = 80)
    Integer sharedWhoamiPort

    @DockerComposeServiceHost(service = "whoami", port = 80)
    String whoamiHost

    @DockerComposeServicePort(service = "whoami", port = 80)
    Integer whoamiPort

    ...
}
```

## General TestContainers usage

See the [TestContainers documentation](https://www.testcontainers.org/) for more information about the underlying library.

# Setup

Snapshot and specific commit version are available using [jitpack.io](https://jitpack.io/).

## Gradle

```gradle
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    compile 'com.github.kiview:spock-docker-extension:-SNAPSHOT'
}

```

# Attributions
The initial version of this project was heavily inspired by the excellent [JUnit5 docker extension](https://github.com/FaustXVI/junit5-docker) by [FaustXVI](https://github.com/FaustXVI).

# License
Copyright 2016 Kevin Wittek

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this project except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

See [LICENSE](LICENSE).