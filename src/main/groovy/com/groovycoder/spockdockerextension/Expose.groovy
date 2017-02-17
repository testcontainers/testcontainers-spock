package com.groovycoder.spockdockerextension

@interface Expose {

    /**
     * @return name of the service to expose
     */
    String service()

    /**
     * @return port to expose
     */
    int port()

    /**
     * @return the referenced instance, defaults to 1
     */
    int instance() default 1
}