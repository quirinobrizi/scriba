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

import static eu.codesketch.rest.scriba.analyser.domain.model.decorator.Decorator.decoratorOrderComparator;
import static eu.codesketch.rest.scriba.analyser.infrastructure.helper.ReflectionHelper.getAllDeclaredDecorators;
import static eu.codesketch.rest.scriba.analyser.infrastructure.helper.ReflectionHelper.getFields;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.codesketch.rest.scriba.analyser.domain.model.Parameter;
import eu.codesketch.rest.scriba.analyser.domain.model.decorator.Decorator;
import eu.codesketch.rest.scriba.analyser.domain.model.document.DocumentBuilder;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.BodyTypeAnnotation;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.Introspector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.IntrospectorManager;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.impl.IntrospectorManagerImpl;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jackson.JsonPropertyAnnotationIntrospector;

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

    @Inject private IntrospectorManager introspectorManager;

    public BodyTypeAnnotationIntrospector() {
        introspectorManager = new IntrospectorManagerImpl(new HashSet<Introspector>(
                        Arrays.asList(new JsonPropertyAnnotationIntrospector())));
    }

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
        java.lang.reflect.Parameter parameter = descriptor
                        .annotatedElementAs(java.lang.reflect.Parameter.class);
        LOGGER.debug("processing annotated element of type {}", parameter.getClass());
        Class<?> parameterType = descriptor.getParameterType();
        if (parameterType.isPrimitive() || String.class.equals(parameterType)) {
            documentBuilder.getOrCreatePayload().addParameter(descriptor.annotatedElement(),
                            new Parameter(parameterType.getTypeName(), null));
        } else {
            doInspectBody(documentBuilder, parameterType);
            if (documentBuilder.getOrCreatePayload().getParameters().isEmpty()) {
                LOGGER.debug("no decorators has been found inspect fields");
                for (Field field : getFields(parameterType)) {
                    documentBuilder.getOrCreatePayload().addParameter(
                                    descriptor.annotatedElement(),
                                    new Parameter(field.getType().getTypeName(), field.getName()));
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
    public Class<BodyTypeAnnotation> type() {
        return BodyTypeAnnotation.class;
    }

    private void doInspectBody(DocumentBuilder documentBuilder, Class<?> body) {
        List<Decorator> decorators = getAllDeclaredDecorators(body);
        Collections.sort(decorators, decoratorOrderComparator());
        for (Decorator decorator : decorators) {
            introspect(documentBuilder, decorator);
        }
    }

    private void introspect(DocumentBuilder documentBuilder, Decorator decorator) {
        Introspector introspector = this.introspectorManager.introspector(decorator
                        .annotationType());
        if (null != introspector) {
            introspector.instrospect(documentBuilder, decorator);
        } else {
            LOGGER.warn("unable instrospect {} as no valid introspector has been found", decorator);
        }
    }
}
