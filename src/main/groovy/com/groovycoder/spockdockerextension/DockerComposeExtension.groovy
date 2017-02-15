package com.groovycoder.spockdockerextension

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.FeatureInfo
import org.spockframework.runtime.model.SpecInfo

class DockerComposeExtension extends AbstractAnnotationDrivenExtension<DockerCompose> {

    @Override
    void visitFeatureAnnotation(DockerCompose annotation, FeatureInfo feature) {
        feature.addInterceptor(new DockerComposeMethodInterceptor(annotation))
    }

    @Override
    void visitSpecAnnotation(DockerCompose annotation, SpecInfo spec) {
        def interceptor = new DockerComposeMethodInterceptor(annotation)
        spec.addSetupInterceptor(interceptor)
        spec.addCleanupInterceptor(interceptor)
        spec.addSetupSpecInterceptor(interceptor)
        spec.addCleanupSpecInterceptor(interceptor)

    }
}
