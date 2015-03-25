package codesketch.scriba.analyser.domain.service.introspector.scriba;

import static java.lang.Boolean.FALSE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import codesketch.scriba.analyser.domain.model.Payload;
import codesketch.scriba.analyser.domain.model.Property;
import codesketch.scriba.analyser.domain.model.decorator.Descriptor;
import codesketch.scriba.analyser.domain.model.document.DocumentBuilder;
import codesketch.scriba.analyser.domain.service.introspector.Introspector;
import codesketch.scriba.analyser.domain.service.introspector.IntrospectorManager;
import codesketch.scriba.annotations.ApiResponse;

@RunWith(MockitoJUnitRunner.class)
public class ApiResponseAnnotationIntrospectorTest {

    @Mock private IntrospectorManager introspectorManager;

    @InjectMocks private ApiResponseAnnotationIntrospector testObj;

    @Mock private DocumentBuilder documentBuilder;
    @Mock private Descriptor descriptor;
    @Mock private ApiResponse apiResponse;
    @Mock private Introspector introspector;
    @Mock private Payload payload;
    @Mock private List<Property> parameterList;

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testInstrospect() {
        Class annotationType = ApiResponse.class;
        Class fieldAnnotationType = JsonProperty.class;
        Class clazz = MessageResponse.class;

        when(descriptor.getWrappedAnnotationAs(annotationType)).thenReturn(apiResponse);
        when(descriptor.annotationType()).thenReturn(annotationType);
        when(apiResponse.type()).thenReturn(clazz);
        when(introspectorManager.introspector(fieldAnnotationType)).thenReturn(introspector);
        when(documentBuilder.getOrCreateResponsePayload()).thenReturn(payload);
        when(payload.getProperties()).thenReturn(parameterList);
        when(parameterList.isEmpty()).thenReturn(FALSE);
        // act
        testObj.instrospect(documentBuilder, descriptor);
        // verify
        verify(descriptor).getWrappedAnnotationAs(annotationType);
        verify(introspectorManager).introspector(fieldAnnotationType);
        verify(introspector).instrospect(eq(documentBuilder), any(Descriptor.class));
    }

    @Test
    public void testType() {
        assertEquals(ApiResponse.class, testObj.type());
    }

    private static class MessageResponse {
        @JsonProperty private String message;
    }

}
