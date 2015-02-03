package eu.codesketch.rest.scriba.analyser.domain.service.introspector;

import static eu.codesketch.rest.scriba.analyser.domain.service.introspector.IntrospectorHelper.introspect;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import eu.codesketch.rest.scriba.analyser.domain.model.decorator.Descriptor;
import eu.codesketch.rest.scriba.analyser.domain.model.document.DocumentBuilder;

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
