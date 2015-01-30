/**
 * Scriba is a software library that aims to analyse REST interface and 
 * produce machine readable documentation.
 *
 * Copyright (C) 2015  Quirino Brizi (quirino.brizi@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.codesketch.rest.scriba.analyser.model;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Represent an annotation with and level that identifies where the annotation
 * is defined in the sub-class chain.
 *
 * @author quirino.brizi
 * @since 29 Jan 2015
 *
 */
public class Decorator {

    private int level;
    private java.lang.annotation.Annotation annotation;
    private AnnotatedElement annotatedElement;

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
     * @param onMethod
     *            a flag that indicates if the onnotation is place on method or
     *            not.
     */
    public Decorator(int level, java.lang.annotation.Annotation annotation, AnnotatedElement annotatedElement) {
        this.level = level;
        this.annotation = annotation;
        this.annotatedElement = annotatedElement;
    }

    public int level() {
        return level;
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
        return null;
    }

    public Class<?> getParameterType() {
        if (Parameter.class.isAssignableFrom(this.annotatedElement.getClass())) {
            return Parameter.class.cast(this.annotatedElement).getType();
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
}
