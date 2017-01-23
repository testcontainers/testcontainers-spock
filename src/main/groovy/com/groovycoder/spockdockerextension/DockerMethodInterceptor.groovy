package com.groovycoder.spockdockerextension

import org.spockframework.runtime.extension.AbstractMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.FieldInfo

import java.lang.reflect.ParameterizedType

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

        spec.getAllFields().each { FieldInfo field ->
            def fieldClass = field.type

            if (fieldClass == DockerClientFacade) {
                field.writeValue(invocation.instance, dockerClient)
            }

            if (isParameterizedSet(fieldClass, field)) {
                ParameterizedType setType = field.reflection.genericType as ParameterizedType

                if (isDockerClientFacadeSet(setType)) {
                    Set clientFacadeSet = (Set) field.readValue(invocation.instance)
                    clientFacadeSet.add(dockerClient)
                }

            }

        }
    }

    private static boolean isDockerClientFacadeSet(ParameterizedType setType) {
        setType.getActualTypeArguments().size() > 0 && setType.getActualTypeArguments()[0] == DockerClientFacade
    }

    private static boolean isParameterizedSet(Class<?> fieldClass, FieldInfo field) {
        fieldClass == Set && field.reflection.genericType instanceof ParameterizedType
    }
}
