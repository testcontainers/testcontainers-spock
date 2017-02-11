package com.groovycoder.spockdockerextension

import com.groovycoder.spockdockerextension.docker.DockerComposeFacade
import org.spockframework.runtime.extension.AbstractMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation

class DockerComposeMethodInterceptor extends AbstractMethodInterceptor {

    private final DockerComposeFacade dockerComposeClient

    DockerComposeMethodInterceptor(DockerCompose dockerCompose) {
        this.dockerComposeClient = new DockerComposeFacade(dockerCompose.value())
    }

    @Override
    void interceptFeatureExecution(IMethodInvocation invocation) throws Throwable {
        wrapInvocationWithDockerComposeUpAndDown(invocation)
    }

    @Override
    void interceptSpecExecution(IMethodInvocation invocation) throws Throwable {
        wrapInvocationWithDockerComposeUpAndDown(invocation)
    }

    private void wrapInvocationWithDockerComposeUpAndDown(IMethodInvocation invocation) {
        dockerComposeClient.up()
        invocation.proceed()
        dockerComposeClient.down()
    }
}
