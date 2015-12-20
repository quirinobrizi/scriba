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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import codesketch.scriba.analyser.infrastructure.helper.StringHelper;

public class StringHelperTest {

    @Test
    public void testJoin() {
        String result = StringHelper.join(Arrays.asList("a", "b", "c"), "/");
        // assert
        assertEquals("a/b/c", result);
    }

}
