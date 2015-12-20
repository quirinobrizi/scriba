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
package codesketch.scriba.analyser.domain.service.introspector.jackson;

import static codesketch.scriba.analyser.domain.service.introspector.IntrospectorHelper.isPrimitiveOrWrapper;
import static codesketch.scriba.analyser.infrastructure.helper.ReflectionHelper.getDescriptorsForAnnotation;
import static codesketch.scriba.analyser.infrastructure.helper.ReflectionHelper.isSetter;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesketch.scriba.analyser.domain.model.Payload;
import codesketch.scriba.analyser.domain.model.Property;
import codesketch.scriba.analyser.domain.model.decorator.Descriptor;
import codesketch.scriba.analyser.domain.model.document.DocumentBuilder;
import codesketch.scriba.analyser.domain.service.introspector.Introspector;

/**
 * Introspect {@link Consumes} annotation and populate the provided
 * {@link DocumentBuilder}.
 *
 * While populating the {@link DocumentBuilder} this introspector will take into
 * account that method level annotations will override the class level one.
 *
 * @author quirino.brizi
 * @since 29 Jan 2015
 *
 */
@Singleton
public class JsonPropertyAnnotationIntrospector implements Introspector {

    private static final Logger LOGGER = LoggerFactory
                    .getLogger(JsonPropertyAnnotationIntrospector.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.codesketch.rest.scriba.analyser.introspector.Introspector#instrospect
     * (eu.codesketch.rest.scriba.analyser.builder.DocumentBuilder,
     * java.lang.Object, int)
     */
    @Override
    public void instrospect(DocumentBuilder documentBuilder, Descriptor descriptor) {
        AnnotatedElement annotatedElement = descriptor.annotatedElement();
        Class<?> parameterType = descriptor.getParameterType();
        Payload payload = descriptor.isResponseInspected()
                        ? documentBuilder.getOrCreateResponsePayload(getDeclaringClass(annotatedElement), "")
                        : documentBuilder.getOrCreateRequestPayload(parameterType, "");
        payload.addParameter(annotatedElement, extractProperty(descriptor));
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.codesketch.rest.scriba.analyser.introspector.Introspector#type()
     */
    @Override
    public Class<JsonProperty> type() {
        return JsonProperty.class;
    }

    private Class<?> getDeclaringClass(AnnotatedElement annotatedElement) {
        if (Member.class.isAssignableFrom(annotatedElement.getClass())) {
            return Member.class.cast(annotatedElement).getDeclaringClass();
        } else {
            return annotatedElement.getClass();
        }
    }

    private Property extractProperty(Descriptor descriptor) {
        JsonProperty jsonProperty = descriptor.getWrappedAnnotationAs(JsonProperty.class);
        String propertyName = jsonProperty.value();
        Class<?> parameterType;
        Field field = descriptor.annotatedElementAs(Field.class);
        if (null != field) {
            parameterType = field.getType();
            propertyName = "".equals(propertyName) ? field.getName() : propertyName;
        } else {
            Method method = descriptor.annotatedElementAs(Method.class);
            if (isSetter(method)) {
                parameterType = method.getParameterTypes()[0];
            } else {
                parameterType = method.getReturnType();
            }
            propertyName = "".equals(propertyName)
                            ? getPropertyNamefromSetterOrGetter(method.getName()) : propertyName;
        }
        LOGGER.debug("JsonProperty annotation has defined property name {}", propertyName);
        if (!isPrimitiveOrWrapper(parameterType)) {
            Property property = new Property(null, propertyName);
            for (Descriptor innerDescriptor : getDescriptorsForAnnotation(parameterType,
                            JsonProperty.class)) {
                property.addProperty(extractProperty(innerDescriptor));
            }
            return property;
        } else {
            return new Property(parameterType.getName(), propertyName);
        }
    }

    private String getPropertyNamefromSetterOrGetter(String input) {
        return StringUtils
                        .replaceEach(input, new String[] { "get", "set" }, new String[] { "", "" })
                        .toLowerCase();
    }
}
