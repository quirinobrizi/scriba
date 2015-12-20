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

package codesketch.scriba;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesketch.scriba.analyser.Scriba;
import codesketch.scriba.analyser.application.impl.AnalyserServiceImplTest.BookStoreInterface;
import codesketch.scriba.analyser.domain.model.Environment;

public class ScribaTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScribaTest.class);

    private Scriba testObj = new Scriba();

    @Test
    public void testDocument() {
        List<Class<?>> interfaces = new ArrayList<>();
        interfaces.add(BookStoreInterface.class);
        Environment env = new Environment();
        env.setName("test");
        env.setEndpoint("http://test.endpoint.org");
        String documents = testObj.document(interfaces, Arrays.asList(env), "1.0.0");
        LOGGER.info("{}", documents);
    }
}
