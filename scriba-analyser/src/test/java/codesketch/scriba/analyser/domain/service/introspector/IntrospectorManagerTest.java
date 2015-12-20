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

package codesketch.scriba.analyser.domain.service.introspector;

import codesketch.scriba.analyser.domain.service.introspector.impl.IntrospectorManagerImpl;
import codesketch.scriba.analyser.domain.service.introspector.jsr311.PathAnnotationIntrospector;
import org.junit.Test;

import javax.ws.rs.Path;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class IntrospectorManagerTest {

    private static final PathAnnotationIntrospector INTROSPECTOR = new PathAnnotationIntrospector();
    private IntrospectorManager testObj = new IntrospectorManagerImpl(
                    new HashSet<Introspector>(Arrays.asList(INTROSPECTOR)));

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
