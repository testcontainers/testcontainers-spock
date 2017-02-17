package com.groovycoder.spockdockerextension

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.FieldInfo

class DockerComposeServicePortExtension extends AbstractAnnotationDrivenExtension<DockerComposeServicePort> {
    @Override
    void visitFieldAnnotation(DockerComposeServicePort annotation, FieldInfo field) {
        // method must be overridden to allow spock access to the annotation
    }
}
