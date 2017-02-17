package com.groovycoder.spockdockerextension

import org.spockframework.runtime.extension.ExtensionAnnotation

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.RUNTIME)
@Target([ElementType.TYPE, ElementType.METHOD])
@ExtensionAnnotation(DockerComposeExtension)
@interface DockerCompose {

    /**
     * @return path to the docker-compose file
     */
    String composeFile()

    /**
     * @return services which should be exposed
     */
    Expose[] exposedServicePorts() default []

    /**
     * @return true if compose environment should be shared between tests, defaults to false
     */
    boolean shared() default false
}