package com.groovycoder.spockdockerextension

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.SpecInfo

class DockerExtension extends AbstractAnnotationDrivenExtension<Docker> {

    @Override
    void visitSpecAnnotation(Docker annotation, SpecInfo spec) {
        spec.addInterceptor(new DockerMethodInterceptor(annotation))
    }
}
