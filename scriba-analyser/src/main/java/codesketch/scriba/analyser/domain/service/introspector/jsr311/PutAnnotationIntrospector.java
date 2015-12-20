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
package codesketch.scriba.analyser.domain.service.introspector.jsr311;

import codesketch.scriba.analyser.domain.model.decorator.Descriptor;
import codesketch.scriba.analyser.domain.model.document.DocumentBuilder;
import codesketch.scriba.analyser.domain.service.introspector.Introspector;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.PUT;

/**
 * Introspect {@link GET} annotation and populate the provided
 * {@link DocumentBuilder}.
 *
 * @author quirino.brizi
 * @since 29 Jan 2015
 *
 */
@Singleton
public class PutAnnotationIntrospector implements Introspector {

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.codesketch.rest.scriba.analyser.introspector.Introspector#instrospect
     * (eu.codesketch.rest.scriba.analyser.builder.DocumentBuilder,
     * java.lang.Object, int)
     */
    @Override
    public void instrospect(DocumentBuilder documentBuilder, Descriptor annotation) {
        documentBuilder.setHttpMethod(HttpMethod.PUT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.codesketch.rest.scriba.analyser.introspector.Introspector#type()
     */
    @Override
    public Class<PUT> type() {
        return PUT.class;
    }

}
