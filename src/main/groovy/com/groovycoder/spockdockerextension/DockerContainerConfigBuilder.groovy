package com.groovycoder.spockdockerextension

class DockerContainerConfigBuilder {

    Docker config

    DockerContainerConfigBuilder(Docker config) {
        this.config = config
    }

    Map build() {
        Map config = [:]
        config += extractPortBindings()
        return config
    }

    Map extractPortBindings() {
        [
                "ExposedPorts": extractExposedPorts(),
                "HostConfig"  : extractHostConfigPortBindings()
        ]
    }

    Map extractExposedPorts() {

        def exposedPorts = [:]
        config.ports().each { String binding ->
            def parsedBinding = parseBinding(binding)
            String innerPort = parsedBinding.inner

            String exposedPort = "$innerPort/tcp"
            exposedPorts.put(exposedPort, [:])

        }

        return exposedPorts
    }

    Map extractHostConfigPortBindings() {

        def hostPortBindings = [:]
        config.ports().each { String binding ->
            def parsedBinding = parseBinding(binding)
            String innerPort = parsedBinding.inner
            String outerPort = parsedBinding.outer

            String exposedPort = "$innerPort/tcp"

            def hostMapping = [["HostIp": "0.0.0.0", "HostPort": "$outerPort"]]

            if (hostPortBindings.containsKey(exposedPort)) {
                hostPortBindings.get(exposedPort).add(hostMapping.first())

            } else {
                hostPortBindings.put(exposedPort, hostMapping)
            }

        }

        [PortBindings: hostPortBindings]
    }


    static Map parseBinding(String binding) {
        def split = binding.split(":")
        [
                outer: split[0],
                inner: split[1]
        ]
    }


}
