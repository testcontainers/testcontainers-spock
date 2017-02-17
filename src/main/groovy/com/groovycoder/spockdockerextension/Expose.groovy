package com.groovycoder.spockdockerextension

@interface Expose {

    /**
     * @return name of the image to start.
     */
    String service()

    /**
     * @return port bindings in docker CLI style syntax
     */
    int port()

    /**
     * @return name under which the container is accessible inside the tests (optional)
     */
    int instance() default 1
}