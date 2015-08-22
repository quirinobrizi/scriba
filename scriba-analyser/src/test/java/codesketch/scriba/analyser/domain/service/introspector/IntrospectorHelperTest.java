package codesketch.scriba.analyser.domain.service.introspector;

import codesketch.scriba.analyser.domain.model.decorator.Descriptor;
import codesketch.scriba.analyser.domain.model.document.DocumentBuilder;
import org.junit.Test;

import static codesketch.scriba.analyser.domain.service.introspector.IntrospectorHelper.introspect;
import static org.mockito.Mockito.*;

public class IntrospectorHelperTest {

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
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
