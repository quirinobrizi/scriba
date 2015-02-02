package eu.codesketch.rest.scriba.analyser.infrastructure.helper;

import static eu.codesketch.rest.scriba.analyser.infrastructure.helper.ReflectionHelper.getAnnotatedMethods;
import static eu.codesketch.rest.scriba.analyser.infrastructure.helper.ReflectionHelper.getClassChain;
import static eu.codesketch.rest.scriba.analyser.infrastructure.helper.ReflectionHelper.getDecorators;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import org.junit.Test;

public class ReflectionHelperTest {

    @Test
    public void testGetAnnotations_annotatedBaseClass() {
        // act
        List<eu.codesketch.rest.scriba.analyser.domain.model.decorator.Decorator> annotations = getDecorators(AnnotatedBaseClass.class);
        // assert
        assertEquals(1, annotations.size());
        eu.codesketch.rest.scriba.analyser.domain.model.decorator.Decorator annotation = annotations.iterator().next();
        assertEquals(Path.class, annotation.annotationType());
        assertEquals(0, annotation.level());
    }

    @Test
    public void testGetAnnotations_annotatedClass() {
        // act
        List<eu.codesketch.rest.scriba.analyser.domain.model.decorator.Decorator> annotations = getDecorators(AnnotatedClass.class);
        // assert
        assertEquals(2, annotations.size());
        int level = annotations.size() - 1;
        for (eu.codesketch.rest.scriba.analyser.domain.model.decorator.Decorator annotation : annotations) {
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
