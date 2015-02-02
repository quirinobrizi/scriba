package eu.codesketch.rest.scriba.analyser.domain.service.introspector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.ws.rs.Path;

import org.junit.Before;
import org.junit.Test;

import eu.codesketch.rest.scriba.analyser.domain.service.introspector.Introspector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.IntrospectorManager;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jsr311.PathAnnotationIntrospector;

public class IntrospectorManagerTest {

    private static final PathAnnotationIntrospector INTROSPECTOR = new PathAnnotationIntrospector();
    private IntrospectorManager testObj = new IntrospectorManager();

    @Before
    public void before() {
        testObj.register(INTROSPECTOR);
    }

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
