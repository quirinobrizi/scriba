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

import codesketch.scriba.analyser.domain.model.decorator.Descriptor;
import codesketch.scriba.analyser.domain.model.document.DocumentBuilder;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;

/**
 * Created by quirino.brizi on 22/08/15.
 */
@Singleton
public class ValidAnnotationIntrospector extends AbstractJSR349AnnotationIntrospector {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidAnnotationIntrospector.class);

    @Override
    public void instrospect(DocumentBuilder documentBuilder, Descriptor descriptor) {
        // NOOP
        LOGGER.info("@Valid annotation introspected, NOOP");
    }

    @Override
    public Class<?> type() {
        return Valid.class;
    }
}
