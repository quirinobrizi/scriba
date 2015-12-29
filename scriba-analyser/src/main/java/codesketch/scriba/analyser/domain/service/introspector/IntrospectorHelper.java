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
package codesketch.scriba.analyser.domain.service.introspector;

import codesketch.scriba.analyser.domain.model.Property;
import codesketch.scriba.analyser.domain.model.decorator.Descriptor;
import codesketch.scriba.analyser.domain.model.document.DocumentBuilder;
import codesketch.scriba.annotations.ApiDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.DefaultValue;
import java.lang.reflect.Field;
import java.util.*;

import static codesketch.scriba.analyser.infrastructure.helper.ReflectionHelper.getFields;

/**
 * A simple helper class that allow to reduce introspector related code
 * duplication.
 *
 * @author quirino.brizi
 * @since 3 Feb 2015
 *
 */
public abstract class IntrospectorHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntrospectorHelper.class);

    private static final List<Class<?>> PRIMITIVE_WRAPPER = Arrays.asList(String.class,
                    Boolean.class, Byte.class, Character.class, Short.class, Integer.class,
                    Long.class, Double.class, Float.class, Void.class, Date.class, Currency.class,
                    List.class, Map.class);

    private IntrospectorHelper() {
    }

    /**
     * Execute introspection based on the provided descriptor.
     *
     * @param introspectorManager
     *            the introspector manager
     * @param documentBuilder
     *            the document builder to pass to the target
     *            {@link Introspector}
     * @param descriptor
     *            the introspection descriptor.
     */
    public static void introspect(IntrospectorManager introspectorManager,
                    DocumentBuilder documentBuilder, Descriptor descriptor) {
        Introspector introspector = introspectorManager.introspector(descriptor.annotationType());
        if (null != introspector) {
            introspector.instrospect(documentBuilder, descriptor);
        } else {
            LOGGER.warn("unable instrospect {} as no valid introspector has been found",
                            descriptor);
        }
    }

    public static Property extractProperty(Field field) {
        Class<?> type = field.getType();
        if (isPrimitiveOrWrapper(type)) {
            return new Property(type.getName(), field.getName());
        } else {
            Property property = new Property(null, field.getName());
            for (Field innerField : getFields(type)) {
                LOGGER.info("Parsing field {} of {}", innerField, type);
                property.addProperty(extractProperty(innerField));
            }
            return property;
        }
    }

    public static boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() || PRIMITIVE_WRAPPER.contains(type);
    }

    public static String getDefaultValueIfAny(Descriptor descriptor) {
        codesketch.scriba.analyser.infrastructure.reflect.Parameter parameter = descriptor
                .annotatedElementAs(
                        codesketch.scriba.analyser.infrastructure.reflect.Parameter.class);
        DefaultValue defaultValueAnnotation = parameter.getAnnotation(DefaultValue.class);
        if (null == defaultValueAnnotation) {
            ApiDefault apiDefault = parameter.getAnnotation(ApiDefault.class);
            return null != apiDefault ? apiDefault.value() : null;
        } else {
            return defaultValueAnnotation.value();
        }
    }
}
