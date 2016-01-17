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
        	LOGGER.info("request payload is a complex object inspectiong it...");
        	List<Descriptor> descriptors = extractAllDescriptors(parameterType);
        	Collections.sort(descriptors, descriptorsOrderComparator());
        	for (Descriptor _descriptor : descriptors) {
        		Field annotatedElement = _descriptor.annotatedElementAs(Field.class);
        		if(null != annotatedElement) {
        			payload.addParameter(annotatedElement, extractProperty(annotatedElement));
        		}
			}
            doInspectBody(documentBuilder, parameterType, descriptors);
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

    private void doInspectBody(DocumentBuilder documentBuilder, Class<?> body, List<Descriptor> descriptors) {
//        List<Descriptor> descriptors = extractAllDescriptors(body);
//        Collections.sort(descriptors, descriptorsOrderComparator());
        for (Descriptor descriptor : descriptors) {
            descriptor.setResponseInspected(FALSE);
            introspect(this.introspectorManager, documentBuilder, descriptor);
        }
    }
}
