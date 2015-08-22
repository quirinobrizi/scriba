package codesketch.scriba.analyser.infrastructure.helper;

import org.junit.Test;

import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static codesketch.scriba.analyser.infrastructure.helper.ReflectionHelper.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReflectionHelperTest {

    @Test
    public void testGetAnnotations_annotatedBaseClass() {
        // act
        List<codesketch.scriba.analyser.domain.model.decorator.Descriptor> annotations = getDescriptors(AnnotatedBaseClass.class);
        // assert
        assertEquals(1, annotations.size());
        codesketch.scriba.analyser.domain.model.decorator.Descriptor annotation = annotations.iterator().next();
        assertEquals(Path.class, annotation.annotationType());
        assertEquals(0, annotation.level());
    }

    @Test
    public void testGetAnnotations_annotatedClass() {
        // act
        List<codesketch.scriba.analyser.domain.model.decorator.Descriptor> annotations = getDescriptors(AnnotatedClass.class);
        // assert
        assertEquals(2, annotations.size());
        int level = annotations.size() - 1;
        for (codesketch.scriba.analyser.domain.model.decorator.Descriptor annotation : annotations) {
            assertEquals(Path.class, annotation.annotationType());
            assertEquals(level--, annotation.level());
            Path path = annotation.getWrappedAnnotationAs(Path.class);
            assertTrue(Arrays.asList("/", "/annotated").contains(path.value()));
        }
    }

    @Test
    public void testGetAnnotatedMethods() {
        // act
        Set<Method> annotatedMethods = getAnnotatedMethods(AnnotatedBaseClass.class);
        // assert
        assertEquals(1, annotatedMethods.size());
    }

    @Test
    public void testGetClassChain() {
        // act
        List<Class<?>> chain = getClassChain(AnnotatedClass.class);
        // assert
        assertEquals(2, chain.size());
        assertEquals(AnnotatedClass.class, chain.get(0));
        assertEquals(AnnotatedBaseClass.class, chain.get(1));
    }

    @Path("/")
    private static class AnnotatedBaseClass {

        @Path("/abc")
        public void baseMethod() {

        }

        @SuppressWarnings("unused")
        public void notAnnotatedMethod() {

        }
    }

    @Path("/annotated")
    private static class AnnotatedClass extends AnnotatedBaseClass {

    }

}
