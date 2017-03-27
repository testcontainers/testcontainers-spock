# TestContainers-Spock
[![Build Status](https://travis-ci.org/testcontainers/testcontainers-spock.svg?branch=master)](https://travis-ci.org/testcontainers/testcontainers-spock)
[![](https://jitpack.io/v/testcontainers/testcontainers-spock.svg)](https://jitpack.io/#testcontainers/testcontainers-spock)

[Spock](https://github.com/spockframework/spock) extension for [TestContainers](https://github.com/testcontainers/testcontainers-java) library, which allows to use Docker containers inside of Spock tests.


# Usage

## @Testcontainers class-annotation

Specifying the `@Testcontainers` annotation will instruct Spock to start and stop all testcontainers accordingly. This annotation 
can be mixed with Spock's `@Shared` annotation to indicate, that containers shouldn't be restarted between tests.

```groovy
@Testcontainers
class TestcontainersSharedContainerIT extends Specification {

    @Shared
    GenericContainer genericContainer = new GenericContainer("emilevauge/whoami:latest")
            .withExposedPorts(80)

    def "starts accessible docker container"() {
        given: "a http client"
        def client = HttpClientBuilder.create().build()
        lastContainerId = genericContainer.containerId

        when: "accessing web server"
        CloseableHttpResponse response = performHttpRequest(client)

        then: "docker container is running and returns http status code 200"
        response.statusLine.statusCode == 200
    }
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
    compile 'com.github.testcontainers:testcontainers-spock:-SNAPSHOT'
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
