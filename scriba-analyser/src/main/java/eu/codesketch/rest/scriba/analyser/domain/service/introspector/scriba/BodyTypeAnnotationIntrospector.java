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
package eu.codesketch.rest.scriba.analyser.domain.service.introspector.scriba;

import static eu.codesketch.rest.scriba.analyser.infrastructure.helper.ReflectionHelper.getDecoratorsForAnnotation;
import static eu.codesketch.rest.scriba.analyser.infrastructure.helper.ReflectionHelper.getFields;
import static eu.codesketch.rest.scriba.analyser.infrastructure.helper.ReflectionHelper.isSetter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.codesketch.rest.scriba.analyser.domain.model.Parameter;
import eu.codesketch.rest.scriba.analyser.domain.model.Payload;
import eu.codesketch.rest.scriba.analyser.domain.model.decorator.Decorator;
import eu.codesketch.rest.scriba.analyser.domain.model.document.DocumentBuilder;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.BodyTypeAnnotation;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.Introspector;

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
public class BodyTypeAnnotationIntrospector implements Introspector {

    private static final Logger LOGGER = LoggerFactory
                    .getLogger(BodyTypeAnnotationIntrospector.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.codesketch.rest.scriba.analyser.introspector.Introspector#instrospect
     * (eu.codesketch.rest.scriba.analyser.builder.DocumentBuilder,
     * java.lang.Object, int)
     */
    @Override
    public void instrospect(DocumentBuilder documentBuilder, Decorator descriptor) {
        Payload payload = new Payload();
        java.lang.reflect.Parameter parameter = descriptor
                        .annotatedElementAs(java.lang.reflect.Parameter.class);
        LOGGER.debug("processing annotated element of type {}", parameter.getClass());
        Class<?> parameterType = descriptor.getParameterType();
        if (parameterType.isPrimitive() || String.class.equals(parameterType)) {
            payload.addParameter(new Parameter(parameterType.getTypeName(), null));
        } else {
            List<Decorator> decorators = getDecoratorsForAnnotation(parameterType,
                            JsonProperty.class);
            if (!decorators.isEmpty()) {
                LOGGER.debug("found {} decorators", decorators.size());
                parseDecorators(payload, decorators);
            } else {
                LOGGER.debug("no decorators has been found inspect fields");
                for (Field field : getFields(parameterType)) {
                    payload.addParameter(new Parameter(field.getType().getTypeName(), field
                                    .getName()));
                }
            }
        }
        documentBuilder.addPayload(payload);
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.codesketch.rest.scriba.analyser.introspector.Introspector#type()
     */
    @Override
    public Class<BodyTypeAnnotation> type() {
        return BodyTypeAnnotation.class;
    }

    private void parseDecorators(Payload payload, List<Decorator> decorators) {
        for (Decorator decorator : decorators) {
            // TODO: inspect JSON/XML annotations
            JsonProperty jsonProperty = decorator.getWrappedAnnotationAs(JsonProperty.class);
            String propertyName = jsonProperty.value();
            String parameterTypeName;
            Field field = decorator.annotatedElementAs(Field.class);
            if (null != field) {
                parameterTypeName = field.getType().getTypeName();
                propertyName = "".equals(propertyName) ? field.getName() : propertyName;
            } else {
                Method method = decorator.annotatedElementAs(Method.class);
                if (isSetter(method)) {
                    parameterTypeName = method.getParameterTypes()[0].getTypeName();
                } else {
                    parameterTypeName = method.getReturnType().getTypeName();
                }
                propertyName = "".equals(propertyName) ? getPropertyNamefromSetterOrGetter(method
                                .getName()) : propertyName;
            }
            LOGGER.debug("JsonProperty annotation has defined property name {}", propertyName);
            payload.addParameter(new Parameter(parameterTypeName, propertyName));
        }
    }

    private String getPropertyNamefromSetterOrGetter(String input) {
        return StringUtils.replaceEach(input, new String[] { "get", "set" },
                        new String[] { "", "" }).toLowerCase();
    }

}
