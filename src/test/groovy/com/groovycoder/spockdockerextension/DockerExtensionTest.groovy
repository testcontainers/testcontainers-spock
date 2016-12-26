package com.groovycoder.spockdockerextension

import org.spockframework.runtime.model.SpecInfo
import spock.lang.Specification

class DockerExtensionTest extends Specification {

    def "adds new interceptor to specification"() {
        given: "the extension"
        def extension = new DockerExtension()

        and: "a annotation"
        def annotation = Stub(Docker)

        and: "a mock specification info"
        def specInfoMock = Mock(SpecInfo)

        when: "visiting specification with these params"
        extension.visitSpecAnnotation(annotation, specInfoMock)

        then: "docker method interceptor with these params is added to spec"
        1 * specInfoMock.addInterceptor(_ as DockerMethodInterceptor)
    }

}
