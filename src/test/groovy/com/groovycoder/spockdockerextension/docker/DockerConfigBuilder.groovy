package com.groovycoder.spockdockerextension.docker

import com.groovycoder.spockdockerextension.Docker
import com.groovycoder.spockdockerextension.Env
import org.testcontainers.containers.wait.Wait

import java.lang.annotation.Annotation

class DockerConfigBuilder {

    String image
    String[] ports = []
    Env[] env = []
    String name = "name"
    Closure waitStrategy = { Wait.defaultWaitStrategy() }

    Docker build () {
        return new Docker() {

            @Override
            String image() {
                return image
            }

            @Override
            String[] ports() {
                return ports
            }

            @Override
            Env[] env() {
                return env
            }

            @Override
            String name() {
                return name
            }

            @Override
            Class waitStrategy() {
                return waitStrategy.class
            }

            @Override
            Class<? extends Annotation> annotationType() {
                return Docker
            }
        }
    }

}
