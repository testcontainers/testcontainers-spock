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
     * @return the referenced instance, defaults to 1
     */
    int instance() default 1
}