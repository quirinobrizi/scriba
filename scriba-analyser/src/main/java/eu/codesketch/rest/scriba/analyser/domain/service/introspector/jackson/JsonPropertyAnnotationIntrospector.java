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
package eu.codesketch.rest.scriba.analyser.domain.service.introspector.jackson;

import static eu.codesketch.rest.scriba.analyser.infrastructure.helper.ReflectionHelper.isSetter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.codesketch.rest.scriba.analyser.domain.model.Parameter;
import eu.codesketch.rest.scriba.analyser.domain.model.Payload;
import eu.codesketch.rest.scriba.analyser.domain.model.decorator.Descriptor;
import eu.codesketch.rest.scriba.analyser.domain.model.document.DocumentBuilder;
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
        JsonProperty jsonProperty = descriptor.getWrappedAnnotationAs(JsonProperty.class);
        String propertyName = jsonProperty.value();
        String parameterTypeName;
        Field field = descriptor.annotatedElementAs(Field.class);
        if (null != field) {
            parameterTypeName = field.getType().getTypeName();
            propertyName = "".equals(propertyName) ? field.getName() : propertyName;
        } else {
            Method method = descriptor.annotatedElementAs(Method.class);
            if (isSetter(method)) {
                parameterTypeName = method.getParameterTypes()[0].getTypeName();
            } else {
                parameterTypeName = method.getReturnType().getTypeName();
            }
            propertyName = "".equals(propertyName) ? getPropertyNamefromSetterOrGetter(method
                            .getName()) : propertyName;
        }
        LOGGER.debug("JsonProperty annotation has defined property name {}", propertyName);
        Payload payload = descriptor.isResponseInspected() ? documentBuilder
                        .getOrCreateResponsePayload() : documentBuilder.getOrCreateRequestPayload();
        payload.addParameter(descriptor.annotatedElement(), new Parameter(parameterTypeName,
                        propertyName));
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

    private String getPropertyNamefromSetterOrGetter(String input) {
        return StringUtils.replaceEach(input, new String[] { "get", "set" },
                        new String[] { "", "" }).toLowerCase();
    }
}