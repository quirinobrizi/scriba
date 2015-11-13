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

import static codesketch.scriba.analyser.domain.model.Message.createMessage;
import static codesketch.scriba.analyser.domain.model.decorator.Descriptor.descriptorsOrderComparator;
import static codesketch.scriba.analyser.domain.service.introspector.IntrospectorHelper.extractProperty;
import static codesketch.scriba.analyser.domain.service.introspector.IntrospectorHelper.introspect;
import static codesketch.scriba.analyser.infrastructure.helper.ReflectionHelper.extractAllDescriptors;
import static codesketch.scriba.analyser.infrastructure.helper.ReflectionHelper.getFields;
import static java.lang.Boolean.TRUE;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesketch.scriba.analyser.domain.model.Payload;
import codesketch.scriba.analyser.domain.model.decorator.Descriptor;
import codesketch.scriba.analyser.domain.model.document.DocumentBuilder;
import codesketch.scriba.analyser.domain.service.introspector.Introspector;
import codesketch.scriba.analyser.domain.service.introspector.IntrospectorManager;
import codesketch.scriba.annotations.ApiResponse;
import codesketch.scriba.annotations.ApiResponses;

/**
 * Introspect {@link ApiResponse} annotation and populate the provided
 * {@link DocumentBuilder}.
 * <p/>
 * While populating the {@link DocumentBuilder} this introspector will take into
 * account that method level annotations will override the class level one.
 *
 * @author quirino.brizi
 * @since 03 Feb 2015
 */
@Singleton
public class ApiResponsesAnnotationIntrospector implements Introspector {

    private static final Logger LOGGER = LoggerFactory
                    .getLogger(ApiResponsesAnnotationIntrospector.class);

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
        ApiResponses apiResponses = descriptor.getWrappedAnnotationAs(ApiResponses.class);
        for (ApiResponse apiResponse : apiResponses.value()) {

            if (null != apiResponse.type()) {
                Payload payload = documentBuilder.getOrCreateResponsePayload(apiResponse.type(),
                                "");
                doInspectBody(documentBuilder, apiResponse.type());

                AnnotatedElement element = descriptor.annotatedElement();
                if (payload.getProperties().isEmpty()) {
                    LOGGER.debug("no decorators has been found inspect fields");
                    for (Field field : getFields(apiResponse.type())) {
                        payload.addParameter(element, extractProperty(field));
                    }
                }
            }
            documentBuilder.addMessage(createMessage(apiResponse.responseCode(),
                            apiResponse.message(), apiResponse.success()));
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.codesketch.rest.scriba.analyser.introspector.Introspector#type()
     */
    @Override
    public Class<ApiResponses> type() {
        return ApiResponses.class;
    }

    private void doInspectBody(DocumentBuilder documentBuilder, Class<?> body) {
        List<Descriptor> descriptors = extractAllDescriptors(body);
        Collections.sort(descriptors, descriptorsOrderComparator());
        for (Descriptor descriptor : descriptors) {
            descriptor.setResponseInspected(TRUE);
            introspect(this.introspectorManager, documentBuilder, descriptor);
        }
    }
}