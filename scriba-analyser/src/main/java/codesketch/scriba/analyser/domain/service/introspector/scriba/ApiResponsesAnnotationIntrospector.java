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
