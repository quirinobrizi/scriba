package eu.codesketch.rest.scriba.analyser.helper;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class StringHelperTest {

    @Test
    public void testJoin() {
        String result = StringHelper.join(Arrays.asList("a", "b", "c"), "/");
        // assert
        assertEquals("a/b/c", result);
    }

}
