package com.groovycoder.spockdockerextension

import org.spockframework.runtime.extension.AbstractMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.FieldInfo

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

        injectDockerFacade(invocation)

        dockerClient.startContainer()
        invocation.proceed()
        dockerClient.stopContainer()
    }

    private void injectDockerFacade(IMethodInvocation invocation) {
        def spec = invocation.getSpec()

        spec.getAllFields().find { FieldInfo field ->
            if (field.type == DockerClientFacade) {
                field.writeValue(invocation.instance, dockerClient)
            }
        }
    }
}
