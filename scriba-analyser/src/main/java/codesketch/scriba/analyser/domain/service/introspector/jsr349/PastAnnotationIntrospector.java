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
package codesketch.scriba.analyser.domain.service.introspector.jsr349;

import codesketch.scriba.analyser.domain.model.ObjectElement;
import codesketch.scriba.analyser.domain.model.decorator.Descriptor;
import codesketch.scriba.analyser.domain.model.document.DocumentBuilder;

import javax.inject.Singleton;
import javax.validation.constraints.Past;
import javax.ws.rs.Consumes;

import static codesketch.scriba.analyser.domain.model.Message.createMessageForBadRequest;

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
public class PastAnnotationIntrospector extends AbstractJSR349AnnotationIntrospector {

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
        Past annotation = descriptor.getWrappedAnnotationAs(type());
        ObjectElement parameter = documentBuilder.getParameter(descriptor);
        parameter.constraints(
                        "date must be in the past considering calendar based on the current timezone and the current locale.");
        documentBuilder.addMessage(
                        createMessageForBadRequest(interpolate(annotation.message(), descriptor)));
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.codesketch.rest.scriba.analyser.introspector.Introspector#type()
     */
    @Override
    public Class<Past> type() {
        return Past.class;
    }

}
