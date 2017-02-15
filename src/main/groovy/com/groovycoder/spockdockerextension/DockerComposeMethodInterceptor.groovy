package com.groovycoder.spockdockerextension

import com.groovycoder.spockdockerextension.docker.DockerComposeFacade
import org.spockframework.runtime.extension.AbstractMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.FieldInfo

class DockerComposeMethodInterceptor extends AbstractMethodInterceptor {

    private final DockerComposeFacade dockerComposeClient
    private final isShared

    DockerComposeMethodInterceptor(DockerCompose dockerCompose) {
        def composeFile = dockerCompose.composeFile()
        def servicePorts = dockerCompose.exposedServicePorts()
        this.isShared = dockerCompose.shared()
        def exposedServiceInstances = servicePorts.collect({
            new ExposedServiceInstance(it.service(), it.port(), it.instance())
        })
        this.dockerComposeClient = new DockerComposeFacade(composeFile, exposedServiceInstances)
    }

    @Override
    void interceptFeatureExecution(IMethodInvocation invocation) throws Throwable {
        wrapInvocationWithDockerComposeUpAndDown(invocation)
    }

    @Override
    void interceptSpecExecution(IMethodInvocation invocation) throws Throwable {
        wrapInvocationWithDockerComposeUpAndDown(invocation)
    }

    @Override
    void interceptSetupMethod(IMethodInvocation invocation) throws Throwable {
        if (isShared) return
        dockerComposeClient.up()
        injectDockerFacade(invocation)
    }

    @Override
    void interceptCleanupMethod(IMethodInvocation invocation) throws Throwable {
        if (isShared) return
        dockerComposeClient.down()
    }

    @Override
    void interceptSetupSpecMethod(IMethodInvocation invocation) throws Throwable {
        if (!isShared) return
        dockerComposeClient.up()
        injectDockerFacade(invocation)
    }

    @Override
    void interceptCleanupSpecMethod(IMethodInvocation invocation) throws Throwable {
        if (!isShared) return
        dockerComposeClient.down()
    }

    private void wrapInvocationWithDockerComposeUpAndDown(IMethodInvocation invocation) {
        invocation.proceed()
    }

    private void injectDockerFacade(IMethodInvocation invocation) {
        def spec = invocation.getSpec()

        spec.getAllFields().each { FieldInfo field ->
            injectIntoField(field, invocation)
        }
    }

    private void injectIntoField(FieldInfo field, IMethodInvocation invocation) {
        def fieldClass = field.type

        switch (fieldClass) {
            case DockerComposeFacade:
                if (this.isShared && field.shared) {
                    field.writeValue(invocation.sharedInstance, dockerComposeClient)
                } else if (!this.isShared && !field.shared) {
                    field.writeValue(invocation.instance, dockerComposeClient)
                }
                break
            case Integer:
                DockerComposeServicePort servicePortAnnotation = field.getAnnotation(DockerComposeServicePort)
                if (servicePortAnnotation) {
                    def port = dockerComposeClient.getServicePort(servicePortAnnotation.service(), servicePortAnnotation.port(), servicePortAnnotation.instance())
                    if (this.isShared && field.shared) {
                        field.writeValue(invocation.sharedInstance, port)
                    } else if (!this.isShared && !field.shared) {
                        field.writeValue(invocation.instance, port)
                    }
                }

                break
            case String:
                DockerComposeServiceHost servicePortAnnotation = field.getAnnotation(DockerComposeServiceHost)
                if (servicePortAnnotation) {
                    def port = dockerComposeClient.getServiceHost(servicePortAnnotation.service(), servicePortAnnotation.port(), servicePortAnnotation.instance())
                    if (this.isShared && field.shared) {
                        field.writeValue(invocation.sharedInstance, port)
                    } else if (!this.isShared && !field.shared) {
                        field.writeValue(invocation.instance, port)
                    }
                }
                break
        }
    }
}
