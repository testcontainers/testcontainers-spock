package com.groovycoder.spockdockerextension

import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.SpecInfo
import spock.lang.Specification

class DockerMethodInterceptorTest extends Specification {

    def "should start container before invoking specification and stop afterwards"() {
        given: "a mock docker client"
        def dockerClientMock = Mock(DockerClientFacade)

        and: "the interceptor"
        def interceptor = new DockerMethodInterceptor(dockerClientMock)

        and: "the method invocation"
        def methodInvocationMock = Mock(IMethodInvocation)
        def specStub = Stub(SpecInfo)
        methodInvocationMock.spec >> specStub
        specStub.allFields >> []

        when: "intercepting the spec"
        interceptor.interceptSpecExecution(methodInvocationMock)

        then: "the container is started"
        1 * dockerClientMock.run()

        then: "the method is invoked"
        1 * methodInvocationMock.proceed()

        then: "the container is stopped"
        1 * dockerClientMock.rm()
    }

}
