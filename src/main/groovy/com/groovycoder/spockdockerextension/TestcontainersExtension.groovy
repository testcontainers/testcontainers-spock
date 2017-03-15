package com.groovycoder.spockdockerextension

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.FeatureInfo
import org.spockframework.runtime.model.MethodInfo
import org.spockframework.runtime.model.SpecInfo

class TestcontainersExtension extends AbstractAnnotationDrivenExtension<Testcontainers> {

    @Override
    void visitSpecAnnotation(Testcontainers annotation, SpecInfo spec) {
        spec.addInterceptor(new TestcontainersMethodInterceptor())
    }

}
