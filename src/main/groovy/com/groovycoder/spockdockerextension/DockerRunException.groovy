package com.groovycoder.spockdockerextension

class DockerRunException extends RuntimeException {
    DockerRunException(String message) {
        super(message)
    }
}
