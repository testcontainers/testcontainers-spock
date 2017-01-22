package com.groovycoder.spockdockerextension

import org.spockframework.runtime.extension.ExtensionAnnotation

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.RUNTIME)
@Target([ElementType.TYPE, ElementType.METHOD])
@ExtensionAnnotation(DockerExtension)
@interface Docker {

    /**
     * @return name of the image to start.
     */
    String image()

    /**
     *
     * @return port bindings in docker CLI style syntax
     */
    String[] ports()

}