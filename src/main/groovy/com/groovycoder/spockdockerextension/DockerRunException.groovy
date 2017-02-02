package com.groovycoder.spockdockerextension

class DockerRunException extends RuntimeException {
    DockerRunException(String s, Throwable throwable) {
        super(s, throwable)
    }
}
