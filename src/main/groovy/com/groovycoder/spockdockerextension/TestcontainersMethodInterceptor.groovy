package com.groovycoder.spockdockerextension

import org.spockframework.runtime.extension.AbstractMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.FieldInfo
import org.testcontainers.containers.GenericContainer

class TestcontainersMethodInterceptor extends AbstractMethodInterceptor {


    TestcontainersMethodInterceptor() {
    }

    @Override
    void interceptSpecExecution(IMethodInvocation invocation) throws Throwable {
        startContainers(invocation)
        invocation.proceed()
        stopContainers(invocation)
    }

    private void startContainers(IMethodInvocation invocation) {

        findContainers(invocation).each { FieldInfo f ->
            GenericContainer container = readContainerFromField(f, invocation)
            container.start()
        }
    }

    private void stopContainers(IMethodInvocation invocation) {

        findContainers(invocation).each { FieldInfo f ->
            GenericContainer container = readContainerFromField(f, invocation)
            container.start()
        }
    }

    private GenericContainer readContainerFromField(FieldInfo f, IMethodInvocation invocation) {
        f.readValue(invocation.instance) as GenericContainer
    }

    private List<FieldInfo> findContainers(IMethodInvocation invocation) {
        invocation.spec.allFields.findAll { FieldInfo f ->
            f.type == GenericContainer
        }
    }

}
