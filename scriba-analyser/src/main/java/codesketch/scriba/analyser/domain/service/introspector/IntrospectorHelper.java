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
package codesketch.scriba.analyser.domain.service.introspector;

import codesketch.scriba.analyser.domain.model.Property;
import codesketch.scriba.analyser.domain.model.decorator.Descriptor;
import codesketch.scriba.analyser.domain.model.document.DocumentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            LOGGER.warn("unable instrospect {} as no valid introspector has been found", descriptor);
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
}
