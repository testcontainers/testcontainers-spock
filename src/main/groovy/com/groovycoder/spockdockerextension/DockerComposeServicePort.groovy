package com.groovycoder.spockdockerextension

import org.spockframework.runtime.extension.ExtensionAnnotation

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
@ExtensionAnnotation(DockerComposeServicePortExtension)
@interface DockerComposeServicePort {

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