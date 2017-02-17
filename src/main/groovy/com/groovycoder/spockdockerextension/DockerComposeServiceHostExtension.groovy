package com.groovycoder.spockdockerextension

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.FieldInfo

class DockerComposeServiceHostExtension extends AbstractAnnotationDrivenExtension<DockerComposeServiceHost> {
    @Override
    void visitFieldAnnotation(DockerComposeServiceHost annotation, FieldInfo field) {
        // method must be overridden to allow spock access to the annotation
    }
}
