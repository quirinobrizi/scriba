/**
 * Scriba is a software library that aims to analyse REST interface and
 * produce machine readable documentation.
 * <p/>
 * Copyright (C) 2015  Quirino Brizi (quirino.brizi@gmail.com)
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package codesketch.scriba.analyser.infrastructure.helper;

import codesketch.scriba.analyser.domain.model.decorator.Descriptor;
import codesketch.scriba.analyser.domain.service.introspector.RequestPayloadAnnotation;
import codesketch.scriba.analyser.infrastructure.reflect.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * Simple utility class for working with the reflection API and handling
 * reflection exceptions.
 *
 * @author quirino.brizi
 * @since 28 Jan 2015
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
     * @return a list containing all the declare {@link Descriptor} or an empty
     *         list if none are found.
     */
    public static List<Descriptor> getDescriptors(Class<?> clazz) {
        List<Descriptor> decorators = new ArrayList<>();
        List<Class<?>> targets = getClassChain(clazz);
        int levels = targets.size() - 1;
        for (int i = levels; i >= 0; i--) {
            Class<?> target = targets.get(i);
            decorators.addAll(toDescriptors(target.getDeclaredAnnotations(), i, target, false));
        }
        return decorators;
    }

    /**
     * Collect all the {@link Descriptor}s declared on the provided method.
     *
     * @param method
     *            the method to inspect
     * @param level
     *            the level that the method has on the chain of path
     *            inheritance.
     * @return a list containing all the declare {@link Descriptor} or an empty
     *         list if none are found.
     */
    public static final List<Descriptor> getDescriptors(Method method, int level) {
        List<Descriptor> decorators = new ArrayList<>();
        decorators.addAll(toDescriptors(method.getDeclaredAnnotations(), level, method, false));
        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            Annotation[] annotations = parameterAnnotations[i];
            Parameter parameter = new Parameter(i, parameterType, annotations);
            if (methodIsAnnotated(annotations)) {
                decorators.addAll(toDescriptors(annotations, level, parameter, true));
            } else {
                LOGGER.debug("found not annotated parameter fallback to custom BodyTypeAnnotation");
                decorators.add(new Descriptor(level, new RequestPayloadAnnotation(), parameter));
            }
        }
        return decorators;
    }

    public static List<Descriptor> extractAllDescriptors(Class<?> clazz) {
        List<Descriptor> decorators = new ArrayList<>();
        List<Class<?>> targets = getClassChain(clazz);
        int levels = targets.size() - 1;
        for (int i = levels; i >= 0; i--) {
            Class<?> target = targets.get(i);
            decorators.addAll(toDescriptors(target.getDeclaredAnnotations(), i, target, false));
            for (Method method : target.getDeclaredMethods()) {
                decorators.addAll(toDescriptors(method.getDeclaredAnnotations(), i + 1, method,
                                false));
            }
            for (Field field : target.getDeclaredFields()) {
                decorators.addAll(
                                toDescriptors(field.getDeclaredAnnotations(), i + 1, field, false));
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
     * @return a list containing all the declare {@link Descriptor} or an empty
     *         list if none are found.
     */
    public static <A extends Annotation> List<Descriptor> getDescriptorsForAnnotation(
                    Class<?> clazz, Class<A> annotation) {
        List<Descriptor> decorators = new ArrayList<>();
        List<Class<?>> targets = getClassChain(clazz);
        int levels = targets.size() - 1;
        for (int i = levels; i >= 0; i--) {
            Class<?> target = targets.get(i);
            A typeAnnotation = target.getAnnotation(annotation);
            if (null != typeAnnotation) {
                decorators.add(new Descriptor(i, typeAnnotation, target));
            }
            for (Method method : target.getDeclaredMethods()) {
                A methodAnnotation = method.getAnnotation(annotation);
                if (null != methodAnnotation) {
                    decorators.add(new Descriptor(i + 1, methodAnnotation, method));
                }
            }
            for (Field field : target.getDeclaredFields()) {
                A fieldAnnotation = field.getAnnotation(annotation);
                if (null != fieldAnnotation) {
                    decorators.add(new Descriptor(i + 1, fieldAnnotation, field));
                }
            }
        }
        return decorators;
    }

    public static final <A extends Annotation> Descriptor getDecoratorsForAnnotation(
                    AccessibleObject accessibleObject, Class<A> annotation) {
        A declaredAnnotation = accessibleObject.getAnnotation(annotation);
        return new Descriptor(0, declaredAnnotation, accessibleObject);

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
        return Modifier.isPublic(method.getModifiers()) && method.getReturnType().equals(void.class)
                        && method.getParameterTypes().length == 1;
    }

    public static Set<Annotation> getMethodAnnotations(Method method) {
        Set<Annotation> declaredAnnotations = new HashSet<Annotation>(
                        Arrays.asList(method.getDeclaredAnnotations()));
        Set<Annotation> parametersAnnotations = new HashSet<>();
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            parametersAnnotations.addAll(Arrays.asList(annotations));
        }
        declaredAnnotations.removeAll(parametersAnnotations);
        return declaredAnnotations;
    }

    /**
     * Wrap all annotations found on the annotated element with
     * {@link Descriptor}.
     *
     * @param annotations
     *            the annotations found on the annotated element
     * @param level
     *            the introspection level
     * @param element
     *            the annotated element currently subject of the parsing
     * @param isParameter
     *            boolean flag identifies if the parsing is currently running
     *            for parameter or other level annotations
     * @return a list of {@link Descriptor}.
     */
    private static List<Descriptor> toDescriptors(Annotation[] annotations, int level,
                    AnnotatedElement element, boolean isParameter) {
        return toDescriptors(new HashSet<Annotation>(Arrays.asList(annotations)), level, element,
                        isParameter);
    }

    private static List<Descriptor> toDescriptors(Set<Annotation> annotations, int level,
                    AnnotatedElement element, boolean isParameter) {
        List<Descriptor> decorators = new ArrayList<>();
        for (Annotation annotation : annotations) {
            if (isParameter && Valid.class.equals(annotation.annotationType())) {
                LOGGER.debug("found annotated parameter containing @Valid annotation, "
                                + "add custom RequestPayloadAnnotation to the introspector list");
                decorators.add(new Descriptor(level, new RequestPayloadAnnotation(), element));
            } else {
                decorators.add(new Descriptor(level, annotation, element));
            }
        }
        return decorators;
    }

    private static boolean methodIsAnnotated(Annotation[] annotations) {
        if (annotations.length >= 1) {
            return true;
        }
        // else if (annotations.length == 1 &&
        // !Valid.class.equals(annotations[0].annotationType())) {
        // return true;
        // }
        return false;
    }
}
