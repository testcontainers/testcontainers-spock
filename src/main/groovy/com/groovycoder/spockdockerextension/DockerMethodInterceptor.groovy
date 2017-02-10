package com.groovycoder.spockdockerextension

import com.groovycoder.spockdockerextension.docker.DockerClientFacade
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
        wrapInvocationWithDockerRunAndRm(invocation)
    }

    @Override
    void interceptFeatureExecution(IMethodInvocation invocation) throws Throwable {
        wrapInvocationWithDockerRunAndRm(invocation)
    }

    private void wrapInvocationWithDockerRunAndRm(IMethodInvocation invocation) {
        dockerClient.run()
        invocation.proceed()
        dockerClient.rm()
    }

    private void injectDockerFacade(IMethodInvocation invocation) {
        def spec = invocation.getSpec()

        spec.getAllFields().each { FieldInfo field ->
            injectIntoField(field, invocation)
        }
    }

    private void injectIntoField(FieldInfo field, IMethodInvocation invocation) {
        def fieldClass = field.type

        if (fieldClass == DockerClientFacade) {
            field.writeValue(invocation.instance, dockerClient)
        }

        if (isParameterizedMap(fieldClass, field)) {
            ParameterizedType mapType = field.reflection.genericType as ParameterizedType

            if (isDockerClientFacadeMap(mapType)) {
                Map clientFacadeMap = field.readValue(invocation.instance) as Map
                clientFacadeMap.put(dockerClient.name, dockerClient)
            }

        }
    }

    private static boolean isDockerClientFacadeMap(ParameterizedType mapType) {
        mapType.actualTypeArguments.size() > 1 &&
                mapType.actualTypeArguments[0] == String &&
                mapType.actualTypeArguments[1] == DockerClientFacade
    }

    private static boolean isParameterizedMap(Class<?> fieldClass, FieldInfo field) {
        fieldClass == Map && field.reflection.genericType instanceof ParameterizedType
    }
}
