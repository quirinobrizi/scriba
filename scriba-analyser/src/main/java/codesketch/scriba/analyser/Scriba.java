/**
 * Scriba is a software library that aims to analyse REST interface and
 * produce machine readable documentation.
 * <p/>
 * Copyright (C) 2015  Quirino Brizi (quirino.brizi@gmail.com)
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

    public String document(List<Class<?>> interfaces, List<Environment> environments, String version) {
        AnalyserService analyser = Guice.createInjector(new ScribaInjector()).getInstance(
                AnalyserService.class);
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
            return mapper.writeValueAsString(new ServiceDescriptorMessage(serviceDescriptor
                    .getVersion(), content));
        } catch (IOException e) {
            LOGGER.error("unable serialize documents");
            throw new RuntimeException(e);
        }
    }
}