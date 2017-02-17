package com.groovycoder.spockdockerextension

import org.spockframework.runtime.InvalidSpecException
import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.FeatureInfo
import org.spockframework.runtime.model.SpecInfo

class DockerComposeExtension extends AbstractAnnotationDrivenExtension<DockerCompose> {

    @Override
    void visitFeatureAnnotation(DockerCompose annotation, FeatureInfo feature) {
        if (annotation.shared()) {
            throw new InvalidSpecException("@%s with shared = true may not be applied to feature methods")
                    .withArgs(DockerCompose.getSimpleName())
        }
        def interceptor = new DockerComposeMethodInterceptor(annotation, feature)
        def spec = feature.spec

        spec.addSetupInterceptor(interceptor)
        spec.addCleanupInterceptor(interceptor)
    }

    @Override
    void visitSpecAnnotation(DockerCompose annotation, SpecInfo spec) {
        def interceptor = new DockerComposeMethodInterceptor(annotation)
        if (annotation.shared()) {
            spec.addSetupSpecInterceptor(interceptor)
            spec.addCleanupSpecInterceptor(interceptor)
        } else {
            spec.addSetupInterceptor(interceptor)
            spec.addCleanupInterceptor(interceptor)
        }
    }
}
