package com.groovycoder.spockdockerextension

/**
 * Reference to a single instance of a service.
 *
 * This class contains the same data as {@link Expose} but is easier to instantiate.
 */
class ExposedServiceInstance {
    /**
     * Name of the service.
     */
    String service

    /**
     * Internal port of the service
     */
    int port

    /**
     * The instance of the service
     */
    int instance

    ExposedServiceInstance(String service, int port) {
        this(service, port, 1)
    }

    ExposedServiceInstance(String service, int port, int instance) {
        this.service = service
        this.port = port
        this.instance = instance
    }
}
