package com.groovycoder.spockdockerextension

import org.spockframework.runtime.extension.AbstractMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation

class DockerMethodInterceptor extends AbstractMethodInterceptor {

    private final DockerClientFacade dockerClient

    DockerMethodInterceptor(Docker docker) {
        this(new DockerClientFacade(docker))
    }

    DockerMethodInterceptor(DockerClientFacade dockerClient) {
        this.dockerClient = dockerClient
    }

    @Override
    void interceptSpecExecution(IMethodInvocation invocation) throws Throwable {
        dockerClient.startContainer()
        invocation.proceed()
        dockerClient.stopContainer()
    }
}
