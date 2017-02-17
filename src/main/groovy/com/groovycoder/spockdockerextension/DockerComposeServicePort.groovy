package com.groovycoder.spockdockerextension

import org.spockframework.runtime.extension.ExtensionAnnotation

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.RUNTIME)
@ExtensionAnnotation(DockerComposeServicePortExtension)
@Target(ElementType.FIELD)
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
     * @return the referenced instance, defaults to 1
     */
    int instance() default 1
}