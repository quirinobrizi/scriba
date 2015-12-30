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

import codesketch.scriba.analyser.domain.model.Property;
import codesketch.scriba.analyser.domain.model.decorator.Descriptor;
import codesketch.scriba.analyser.domain.model.document.DocumentBuilder;
import codesketch.scriba.annotations.ApiIndirectParameter;
import codesketch.scriba.annotations.ApiResponse;
import org.hibernate.validator.constraints.ModCheck;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by quirino on 25/12/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class ApiIndirectParameterIntrospectorTest {

    private ApiIndirectParameterIntrospector testObj = new ApiIndirectParameterIntrospector();

    @Mock private DocumentBuilder documentBuilder;
    @Mock private Descriptor descriptor;
    @Mock private ApiIndirectParameter apiIndirectParameter;
    @Mock private AnnotatedElement annotatedElement;
    @Captor private ArgumentCaptor<Property> captor;

    @Test
    public void testInstrospect() throws Exception {
        Class annotationType = ApiIndirectParameter.class;
        Class clazz = String.class;

        when(descriptor.getWrappedAnnotationAs(annotationType)).thenReturn(apiIndirectParameter);
        when(descriptor.annotatedElement()).thenReturn(annotatedElement);

        when(apiIndirectParameter.annotationType()).thenReturn(annotationType);
        when(apiIndirectParameter.objectType()).thenReturn(clazz);
        when(apiIndirectParameter.type()).thenReturn(ApiIndirectParameter.Type.HEADER);
        when(apiIndirectParameter.defaultValue()).thenReturn("defaultValue");
        when(apiIndirectParameter.description()).thenReturn("description");
        when(apiIndirectParameter.value()).thenReturn("authorization");
        when(apiIndirectParameter.constraint()).thenReturn("[a-z]+");

        // act
        testObj.instrospect(documentBuilder, descriptor);
        // verify
        verify(documentBuilder).putHeaderParameter(eq(annotatedElement), captor.capture());
        // assert
        Property property = captor.getValue();
        assertEquals("[a-z]+", property.getConstraints().get(0).constraint());
        assertEquals("string", property.getType());
        assertEquals("authorization", property.getName());
        assertEquals("defaultValue", property.getDefaultValue());
        assertEquals("description", property.getDescription());
    }

    @Test
    public void testType() throws Exception {
        assertEquals(ApiIndirectParameter.class, testObj.type());
    }
}