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
package codesketch.scriba.analyser.domain.service.introspector.scriba;

import codesketch.scriba.analyser.domain.model.Payload;
import codesketch.scriba.analyser.domain.model.Property;
import codesketch.scriba.analyser.domain.model.decorator.Descriptor;
import codesketch.scriba.analyser.domain.model.document.DocumentBuilder;
import codesketch.scriba.analyser.domain.service.introspector.Introspector;
import codesketch.scriba.analyser.domain.service.introspector.IntrospectorManager;
import codesketch.scriba.analyser.domain.service.introspector.RequestPayloadAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static codesketch.scriba.analyser.domain.model.decorator.Descriptor.descriptorsOrderComparator;
import static codesketch.scriba.analyser.domain.service.introspector.IntrospectorHelper.*;
import static codesketch.scriba.analyser.infrastructure.helper.ReflectionHelper.extractAllDescriptors;
import static codesketch.scriba.analyser.infrastructure.helper.ReflectionHelper.getFields;
import static java.lang.Boolean.FALSE;

/**
 * Introspect {@link Consumes} annotation and populate the provided
 * {@link DocumentBuilder}.
 * <p/>
 * While populating the {@link DocumentBuilder} this introspector will take into
 * account that method level annotations will override the class level one.
 *
 * @author quirino.brizi
 * @since 29 Jan 2015
 */
@Singleton
public class RequestPayloadAnnotationIntrospector implements Introspector {

    private static final Logger LOGGER = LoggerFactory
                    .getLogger(RequestPayloadAnnotationIntrospector.class);

    @Inject private IntrospectorManager introspectorManager;

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
        codesketch.scriba.analyser.infrastructure.reflect.Parameter parameter = descriptor
                        .annotatedElementAs(
                                        codesketch.scriba.analyser.infrastructure.reflect.Parameter.class);
        LOGGER.debug("processing annotated element of type {}", parameter.getClass());
        Class<?> parameterType = descriptor.getParameterType();
        AnnotatedElement element = descriptor.annotatedElement();
        Payload payload = documentBuilder.getOrCreateRequestPayload(parameterType, "");
        if (isPrimitiveOrWrapper(parameterType)) {
            payload.addParameter(element, new Property(parameterType.getName(), null));
        } else {
            doInspectBody(documentBuilder, parameterType);
            if (payload.getProperties().isEmpty()) {
                LOGGER.debug("no decorators has been found inspect fields");
                for (Field field : getFields(parameterType)) {
                    payload.addParameter(element, extractProperty(field));
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.codesketch.rest.scriba.analyser.introspector.Introspector#type()
     */
    @Override
    public Class<RequestPayloadAnnotation> type() {
        return RequestPayloadAnnotation.class;
    }

    private void doInspectBody(DocumentBuilder documentBuilder, Class<?> body) {
        List<Descriptor> descriptors = extractAllDescriptors(body);
        Collections.sort(descriptors, descriptorsOrderComparator());
        for (Descriptor descriptor : descriptors) {
            descriptor.setResponseInspected(FALSE);
            introspect(this.introspectorManager, documentBuilder, descriptor);
        }
    }
}
