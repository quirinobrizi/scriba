package codesketch.scriba.analyser.domain.service.introspector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashSet;

import javax.ws.rs.Path;

import org.junit.Test;

import codesketch.scriba.analyser.domain.service.introspector.Introspector;
import codesketch.scriba.analyser.domain.service.introspector.IntrospectorManager;
import codesketch.scriba.analyser.domain.service.introspector.impl.IntrospectorManagerImpl;
import codesketch.scriba.analyser.domain.service.introspector.jsr311.PathAnnotationIntrospector;

public class IntrospectorManagerTest {

    private static final PathAnnotationIntrospector INTROSPECTOR = new PathAnnotationIntrospector();
    private IntrospectorManager testObj = new IntrospectorManagerImpl(new HashSet<Introspector>(
                    Arrays.asList(INTROSPECTOR)));

    @Test
    public void testIntrospector() {
        // act
        Introspector introspector = testObj.introspector(Path.class);
        // assert
        assertEquals(INTROSPECTOR, introspector);
    }

    @Test
    public void testIntrospector_failure() {
        // act
        assertNull(testObj.introspector(Object.class));
    }

}
