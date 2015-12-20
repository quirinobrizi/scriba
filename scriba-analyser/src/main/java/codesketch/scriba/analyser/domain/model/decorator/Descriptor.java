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
package codesketch.scriba.analyser.domain.model.decorator;

import static codesketch.scriba.analyser.domain.model.decorator.Order.lookupOrder;
import static java.lang.Boolean.FALSE;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Describes the context on which the introspection is executed.
 *
 * @author quirino.brizi
 * @since 29 Jan 2015
 */
public class Descriptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Descriptor.class);

    private int order;
    private int level;
    private java.lang.annotation.Annotation annotation;
    private AnnotatedElement annotatedElement;
    private Boolean response;

    /**
     * Create a new wrapper for {@link java.lang.annotation.Annotation} setting
     * its level.
     *
     * @param level
     *            the level on the class chain the annotation is declared, level
     *            0 means the base class level n identifies the last
     *            implementation/extension.
     * @param annotation
     *            the annotation to wrap.
     * @param annotatedElement
     *            The annotoated element.
     */
    public Descriptor(int level, java.lang.annotation.Annotation annotation,
                    AnnotatedElement annotatedElement) {
        this.level = level;
        this.annotation = annotation;
        this.annotatedElement = annotatedElement;
        this.order = lookupOrder(annotation.annotationType());
        this.response = FALSE;
        LOGGER.info("created new descriptor for annotation {} with order {}", annotation,
                        this.order);
    }

    public static Comparator<Descriptor> descriptorsOrderComparator() {
        return new Comparator<Descriptor>() {

            @Override
            public int compare(Descriptor o1, Descriptor o2) {
                return o2.order() - o1.order();
            }
        };
    }

    public int level() {
        return level;
    }

    public int order() {
        return order;
    }

    public void setResponseInspected(Boolean response) {
        this.response = response;
    }

    public Boolean isResponseInspected() {
        return response;
    }

    public Boolean isOnMethod() {
        return Method.class.isAssignableFrom(this.annotatedElement.getClass());
    }

    public AnnotatedElement annotatedElement() {
        return annotatedElement;
    }

    public <T> T annotatedElementAs(Class<T> type) {
        if (type.isAssignableFrom(annotatedElement.getClass())) {
            return type.cast(annotatedElement);
        }
        LOGGER.warn("Annotated element {} is not assignable from the requested type {}",
                        annotatedElement, type);
        return null;
    }

    public Class<?> getParameterType() {
        if (codesketch.scriba.analyser.infrastructure.reflect.Parameter.class
                        .isAssignableFrom(this.annotatedElement.getClass())) {
            return codesketch.scriba.analyser.infrastructure.reflect.Parameter.class
                            .cast(this.annotatedElement).getType();
        } else if (Method.class.isAssignableFrom(this.annotatedElement.getClass())) {
            return Method.class.cast(this.annotatedElement).getReturnType();
        } else if (Field.class.isAssignableFrom(this.annotatedElement.getClass())) {
            return Field.class.cast(this.annotatedElement).getType();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends java.lang.annotation.Annotation> T getAnnotation() {
        return (T) annotation;
    }

    public Class<? extends java.lang.annotation.Annotation> annotationType() {
        return annotation.annotationType();
    }

    public <T extends java.lang.annotation.Annotation> T getWrappedAnnotationAs(Class<T> type) {
        if (type.isAssignableFrom(annotationType())) {
            return type.cast(annotation);
        }
        throw new ClassCastException(String.format("unable cast %s to %s", annotationType(), type));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Decorator [level=").append(level).append(", annotation=").append(annotation)
                        .append(", annotatedElement=").append(annotatedElement).append("]");
        return builder.toString();
    }
}
