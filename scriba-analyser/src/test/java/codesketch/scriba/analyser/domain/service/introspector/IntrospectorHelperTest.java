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

package codesketch.scriba.analyser.domain.service.introspector;

import codesketch.scriba.analyser.domain.model.decorator.Descriptor;
import codesketch.scriba.analyser.domain.model.document.DocumentBuilder;
import org.junit.Test;

import static codesketch.scriba.analyser.domain.service.introspector.IntrospectorHelper.introspect;
import static org.mockito.Mockito.*;

public class IntrospectorHelperTest {

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testIntrospect() {
        IntrospectorManager introspectorManager = mock(IntrospectorManager.class);
        DocumentBuilder documentBuilder = mock(DocumentBuilder.class);
        Descriptor descriptor = mock(Descriptor.class);
        Introspector introspector = mock(Introspector.class);

        Class clazz = RequestPayloadAnnotation.class;
        when(descriptor.annotationType()).thenReturn(clazz);
        when(introspectorManager.introspector(clazz)).thenReturn(introspector);
        // act
        introspect(introspectorManager, documentBuilder, descriptor);
        // verify
        verify(introspectorManager).introspector(clazz);
        verify(descriptor).annotationType();
        verify(introspector).instrospect(documentBuilder, descriptor);
    }
}
