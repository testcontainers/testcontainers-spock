package com.groovycoder.spockdockerextension

import com.groovycoder.spockdockerextension.docker.DockerComposeFacade
import org.spockframework.runtime.extension.AbstractMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.FeatureInfo
import org.spockframework.runtime.model.FieldInfo

class DockerComposeMethodInterceptor extends AbstractMethodInterceptor {

    private final DockerComposeFacade dockerComposeClient
    private final boolean isShared
    private final FeatureInfo feature

    DockerComposeMethodInterceptor(DockerCompose dockerCompose, FeatureInfo feature) {
        def composeFile = dockerCompose.composeFile()
        def servicePorts = dockerCompose.exposedServicePorts()
        this.isShared = dockerCompose.shared()
        def exposedServiceInstances = servicePorts.collect({
            new ExposedServiceInstance(it.service(), it.port(), it.instance())
        })
        this.dockerComposeClient = new DockerComposeFacade(composeFile, exposedServiceInstances)
        this.feature = feature
    }

    DockerComposeMethodInterceptor(DockerCompose dockerCompose) {
        this(dockerCompose, null)
    }

    @Override
    void interceptSetupMethod(IMethodInvocation invocation) throws Throwable {
        if (invocationMatches(invocation)) {
            dockerComposeClient.up()
            injectIntoAllFieldsOfSpec(invocation)
        }
        invocation.proceed()
    }

    @Override
    void interceptCleanupMethod(IMethodInvocation invocation) throws Throwable {
        if (invocationMatches(invocation)) {
            dockerComposeClient.down()
        }
        invocation.proceed()
    }

    @Override
    void interceptSetupSpecMethod(IMethodInvocation invocation) throws Throwable {
        if (invocationMatches(invocation)) {
            dockerComposeClient.up()
            injectIntoAllFieldsOfSpec(invocation)
        }
        invocation.proceed()
    }

    @Override
    void interceptCleanupSpecMethod(IMethodInvocation invocation) throws Throwable {
        if (invocationMatches(invocation)) {
            dockerComposeClient.down()
        }
        invocation.proceed()
    }

    private boolean invocationMatches(IMethodInvocation invocation) {
        if (feature == null) {
            // this interceptor is not bound to a specific feature and thus matches
            return true
        }

        if (feature == invocation.feature) {
            // this interceptor is not bound to a specific feature which matches the feature of the invocation
            return true
        }

        // this interceptor is bound to a specific feature but the invocation is for a different feature
        return false
    }

    private void injectIntoAllFieldsOfSpec(IMethodInvocation invocation) {
        def spec = invocation.getSpec()

        spec.getAllFields().each { FieldInfo field ->
            injectIntoField(field, invocation)
        }
    }

    /**
     * Write to the {@link IMethodInvocation#getSharedInstance()} if this is a shared docker compose, otherwise write to {@link IMethodInvocation#getInstance()}
     * @param field
     * @param invocation
     * @param value
     */
    private void writeSharedOrIsolated(FieldInfo field, IMethodInvocation invocation, Object value) {
        final instance
        if (this.isShared && field.shared) {
            instance = invocation.sharedInstance
            field.writeValue(instance, value)
        } else if (!this.isShared && !field.shared) {
            instance = invocation.instance
            field.writeValue(instance, value)
        }
    }

    private void injectIntoField(FieldInfo field, IMethodInvocation invocation) {
        def fieldClass = field.type

        switch (fieldClass) {
            case DockerComposeFacade:
                writeSharedOrIsolated(field, invocation, dockerComposeClient)
                break
            case Integer:
                DockerComposeServicePort servicePortAnnotation = field.getAnnotation(DockerComposeServicePort)
                if (servicePortAnnotation) {
                    def port = dockerComposeClient.getServicePort(servicePortAnnotation.service(), servicePortAnnotation.port(), servicePortAnnotation.instance())
                    writeSharedOrIsolated(field, invocation, port)
                }

                break
            case String:
                DockerComposeServiceHost serviceHostAnnotation = field.getAnnotation(DockerComposeServiceHost)
                if (serviceHostAnnotation) {
                    def host = dockerComposeClient.getServiceHost(serviceHostAnnotation.service(), serviceHostAnnotation.port(), serviceHostAnnotation.instance())
                    writeSharedOrIsolated(field, invocation, host)
                }
                break
        }
    }
}
