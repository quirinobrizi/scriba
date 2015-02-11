package eu.codesketch.scriba.rest.analyser.infrastructure.helper;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import javax.validation.MessageInterpolator;
import javax.validation.MessageInterpolator.Context;
import javax.validation.metadata.ConstraintDescriptor;

import org.hibernate.validator.internal.engine.MessageInterpolatorContext;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.core.ConstraintOrigin;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;

import eu.codesketch.scriba.rest.analyser.domain.model.decorator.Descriptor;

public class MessageInterpolatorHelper {

    private MessageInterpolator messageInterpolator = new ResourceBundleMessageInterpolator();

    public String interpolate(String message, Descriptor descriptor) {
        ConstraintDescriptor<?> constraintDescriptor = createConstraintDescriptor(descriptor);
        Object validatedValue = null;
        Class<?> rootBeanType = descriptor.getParameterType();
        Context context = new MessageInterpolatorContext(constraintDescriptor, validatedValue,
                        rootBeanType);
        return messageInterpolator.interpolate(message, context);
    }

    private ConstraintDescriptor<?> createConstraintDescriptor(Descriptor descriptor) {
        Member member = extractMemberForm(descriptor.annotatedElement());
        Annotation annotation = descriptor.getAnnotation();
        ConstraintHelper constraintHelper = new ConstraintHelper();
        ElementType type = null;
        ConstraintOrigin definedOn = ConstraintOrigin.DEFINED_LOCALLY;
        return new ConstraintDescriptorImpl<Annotation>(member, annotation, constraintHelper, type,
                        definedOn);
    }

    private Member extractMemberForm(AnnotatedElement annotatedElement) {
        if (Method.class.isAssignableFrom(annotatedElement.getClass())) {
            return Method.class.cast(annotatedElement);
        } else if (Field.class.isAssignableFrom(annotatedElement.getClass())) {
            return Field.class.cast(annotatedElement);
        } else if (Constructor.class.isAssignableFrom(annotatedElement.getClass())) {
            return Constructor.class.cast(annotatedElement);
        }
        return null;
    }
}
