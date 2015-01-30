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
package eu.codesketch.rest.scriba.analyser.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.codesketch.rest.scriba.analyser.introspector.BodyTypeAnnotation;
import eu.codesketch.rest.scriba.analyser.model.Decorator;

/**
 * Simple utility class for working with the reflection API and handling
 * reflection exceptions.
 *
 * @author quirino.brizi
 * @since 28 Jan 2015
 *
 */
public abstract class ReflectionHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionHelper.class);

    private ReflectionHelper() {
    }

    /**
     * Collect all the decorators declared on the provided class.
     * 
     * @param clazz
     *            the class to introspect
     * @return a list containing all the declare {@link Decorator} or an empty
     *         list if none are found.
     */
    public static List<Decorator> getDecorators(Class<?> clazz) {
        List<Decorator> decorators = new ArrayList<>();
        List<Class<?>> targets = getClassChain(clazz);
        int levels = targets.size() - 1;
        for (int i = levels; i >= 0; i--) {
            Class<?> target = targets.get(i);
            decorators.addAll(toDecorators(target.getDeclaredAnnotations(), i, target));
        }
        return decorators;
    }

    /**
     * Collect all the {@link Decorator}s declared on the provided method.
     * 
     * @param method
     *            the method to inspect
     * @param level
     *            the level that the method has on the chain of path
     *            inheritance.
     * @return a list containing all the declare {@link Decorator} or an empty
     *         list if none are found.
     */
    public static final List<Decorator> getDecorators(Method method, int level) {
        List<Decorator> decorators = new ArrayList<>();
        decorators.addAll(toDecorators(method.getDeclaredAnnotations(), level, method));
        for (Parameter parameter : method.getParameters()) {
            Annotation[] annotations = parameter.getAnnotations();
            if (annotations.length > 0) {
                decorators.addAll(toDecorators(annotations, level, parameter));
            } else {
                LOGGER.debug("found not annotated parameter fallback to custom BodyTypeAnnotation");
                decorators.add(new Decorator(level, new BodyTypeAnnotation(), parameter));
            }
        }
        return decorators;
    }

    /**
     * Retrieve all the decorators for the requested annotation defined on the
     * declared class
     * 
     * @param clazz
     *            the target class
     * @param annotation
     *            the target annotation
     * @return a list containing all the declare {@link Decorator} or an empty
     *         list if none are found.
     */
    public static <A extends Annotation> List<Decorator> getDecoratorsForAnnotation(Class<?> clazz,
                    Class<A> annotation) {
        List<Decorator> decorators = new ArrayList<>();
        List<Class<?>> targets = getClassChain(clazz);
        int levels = targets.size() - 1;
        for (int i = levels; i >= 0; i--) {
            Class<?> target = targets.get(i);
            A typeAnnotation = target.getDeclaredAnnotation(annotation);
            if (null != typeAnnotation) {
                decorators.add(new Decorator(i, typeAnnotation, target));
            }
            for (Method method : target.getDeclaredMethods()) {
                A methodAnnotation = method.getDeclaredAnnotation(annotation);
                if (null != methodAnnotation) {
                    decorators.add(new Decorator(i + 1, methodAnnotation, method));
                }
            }
            for (Field field : target.getDeclaredFields()) {
                A fieldAnnotation = field.getDeclaredAnnotation(annotation);
                if (null != fieldAnnotation) {
                    decorators.add(new Decorator(i + 1, fieldAnnotation, field));
                }
            }
        }
        return decorators;
    }

    /**
     * Collect all the annotated methods declared on the provided class.
     * 
     * @param clazz
     *            the class to introspect
     * @return a list containing all the declare methods or an empty list if
     *         none are found.
     */
    public static Set<Method> getAnnotatedMethods(Class<?> clazz) {
        Set<Method> annotations = new HashSet<>();
        Class<?> target = clazz;
        while (null != target) {
            Method[] declaredMethods = target.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.getDeclaredAnnotations().length > 0) {
                    annotations.add(method);
                }
            }
            target = target.getSuperclass();
        }
        return annotations;
    }

    public static List<Field> getFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> target : getClassChain(clazz)) {
            for (Field field : target.getDeclaredFields()) {
                fields.add(field);
            }
        }
        return fields;
    }

    /**
     * Return the chain of class starting from the provided one as bottom
     * position. i.e. if classA is provided and classA extends ClassB that
     * implements InterfaceA the returned list will be ClassA, ClassB,
     * InterfaceA.<br />
     * The method will exclude Object class from the chain
     * 
     * @param clazz
     *            the class to introspect
     * @return the list of classes that composes the inheritance chain starting
     *         from the provided one.
     */
    public static List<Class<?>> getClassChain(Class<?> clazz) {
        List<Class<?>> chain = new ArrayList<>();
        Class<?> target = clazz;
        while (null != target && !Object.class.equals(target)) {
            chain.add(target);
            target = target.getSuperclass();
        }
        return chain;
    }

    public static boolean isSetter(Method method) {
        return Modifier.isPublic(method.getModifiers())
                        && method.getReturnType().equals(void.class)
                        && method.getParameterTypes().length == 1;
        // && method.getName().matches("^set[A-Z].*");
    }

    private static List<Decorator> toDecorators(Annotation[] annotations, int level,
                    AnnotatedElement annotatedElement) {
        List<Decorator> decorators = new ArrayList<>();
        for (Annotation annotation : annotations) {
            decorators.add(new Decorator(level, annotation, annotatedElement));
        }
        return decorators;
    }
}
