/*
 * Copyright [2015] [Quirino Brizi (quirino.brizi@gmail.com)]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package codesketch.scriba.analyser.infrastructure.helper;

import codesketch.scriba.analyser.domain.model.decorator.Descriptor;
import org.hibernate.validator.internal.engine.MessageInterpolatorContext;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.core.ConstraintOrigin;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;

import javax.validation.MessageInterpolator;
import javax.validation.MessageInterpolator.Context;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.*;

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
