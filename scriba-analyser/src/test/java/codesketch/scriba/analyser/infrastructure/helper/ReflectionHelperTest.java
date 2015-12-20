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
        List<codesketch.scriba.analyser.domain.model.decorator.Descriptor> annotations = getDescriptors(
                        AnnotatedBaseClass.class);
        // assert
        assertEquals(1, annotations.size());
        codesketch.scriba.analyser.domain.model.decorator.Descriptor annotation = annotations
                        .iterator().next();
        assertEquals(Path.class, annotation.annotationType());
        assertEquals(0, annotation.level());
    }

    @Test
    public void testGetAnnotations_annotatedClass() {
        // act
        List<codesketch.scriba.analyser.domain.model.decorator.Descriptor> annotations = getDescriptors(
                        AnnotatedClass.class);
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
