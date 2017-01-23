package com.groovycoder.spockdockerextension

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.FeatureInfo
import org.spockframework.runtime.model.SpecInfo

class DockerContainersExtension extends AbstractAnnotationDrivenExtension<DockerContainers> {

    @Override
    void visitSpecAnnotation(DockerContainers annotation, SpecInfo spec) {
        annotation.value().each {
            spec.addInterceptor(new DockerMethodInterceptor(it))
        }
    }

    @Override
    void visitFeatureAnnotation(DockerContainers annotation, FeatureInfo feature) {
        annotation.value().each {
            feature.addInterceptor(new DockerMethodInterceptor(it))
        }
    }
}
