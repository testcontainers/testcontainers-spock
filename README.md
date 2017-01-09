# spock-docker-extension
[![Build Status](https://travis-ci.org/kiview/spock-docker-extension.svg?branch=travis)](https://travis-ci.org/kiview/spock-docker-extension)
[![](https://jitpack.io/v/kiview/spock-docker-extension.svg)](https://jitpack.io/#kiview/spock-docker-extension)

Start docker containers for your Spock tests.

This project is heavily inspired by the excellent JUnit5 docker extension by [FaustXVI](https://github.com/FaustXVI):
https://github.com/FaustXVI/junit5-docker

You can use the extension like this:

```groovy
@Docker(image = "nginx", ports = ["8080:80"])
class DockerExtensionIT extends Specification {
   // tests
}
```

Have a look into the included integration tests to see more examples.

# Usage

Add the dependency using jitpack.io:

```gradle
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
    maven { url 'http://dl.bintray.com/gesellix/docker-utils' }
}

dependencies {
    compile 'com.github.kiview:spock-docker-extension:-SNAPSHOT'
}

```
