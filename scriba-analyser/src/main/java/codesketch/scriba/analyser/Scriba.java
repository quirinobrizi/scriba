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

package codesketch.scriba.analyser;

import codesketch.scriba.analyser.application.AnalyserService;
import codesketch.scriba.analyser.domain.model.Environment;
import codesketch.scriba.analyser.domain.model.ServiceDescriptor;
import codesketch.scriba.analyser.domain.model.document.Document;
import codesketch.scriba.analyser.infrastructure.guice.ScribaInjector;
import codesketch.scriba.analyser.infrastructure.message.ServiceDescriptorMessage;
import com.google.inject.Guice;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * entry point for analysing and generate documentation for RESTfull API.
 *
 * @author quirino.brizi
 * @since 30 Jan 2015
 *
 */
public class Scriba {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scriba.class);

    // private AnalyserService analyser;
    private ObjectMapper mapper;

    public Scriba() {
        // this.analyser = new AnalyserServiceImpl();
        this.mapper = new ObjectMapper();
    }

    public String document(List<Class<?>> interfaces, List<Environment> environments,
                    String version) {
        AnalyserService analyser = Guice.createInjector(new ScribaInjector())
                        .getInstance(AnalyserService.class);
        List<Document> documents = new ArrayList<>();
        for (Class<?> target : interfaces) {
            LOGGER.info("analysing class {}", target);
            documents.addAll(analyser.analyse(target));
        }
        return serialize(new ServiceDescriptor(version, environments, documents));
    }

    private String serialize(ServiceDescriptor serviceDescriptor) {
        try {
            String content = mapper.writeValueAsString(serviceDescriptor);
            return mapper.writeValueAsString(
                            new ServiceDescriptorMessage(serviceDescriptor.getVersion(), content));
        } catch (IOException e) {
            LOGGER.error("unable serialize documents");
            throw new RuntimeException(e);
        }
    }
}
