# spock-docker-extension
[![CircleCI](https://circleci.com/gh/kiview/spock-docker-extension.svg?style=svg)](https://circleci.com/gh/kiview/spock-docker-extension)
[![Release](https://jitpack.io/v/kiview/spock-docker-extension.svg)]
(https://jitpack.io/#User/Repo)

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
    maven { url "https://jitpack.io" }
}
dependencies {
     compile 'com.github.jitpack:gradle-simple:1.0'
}
```