/**
 * Scriba is a software library that aims to analyse REST interface and 
 * produce machine readable documentation.
 *
 * Copyright (C) 2015  Quirino Brizi (quirino.brizi@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.codesketch.rest.scriba.analyser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.codesketch.rest.scriba.analyser.impl.Jsr311Analyser;
import eu.codesketch.rest.scriba.analyser.model.Document;

/**
 * entry point for analysing and generate documentation for RESTfull API.
 *
 * @author quirino.brizi
 * @since 30 Jan 2015
 *
 */
public class Scriba {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scriba.class);

    private Analyser analyser;
    private ObjectMapper mapper;

    public Scriba() {
        this.analyser = new Jsr311Analyser();
        this.mapper = new ObjectMapper();
    }

    public String document(List<Class<?>> interfaces) {
        List<Document> documents = new ArrayList<>();
        for (Class<?> target : interfaces) {
            LOGGER.info("analysing class {}", target);
            documents.addAll(analyser.analyse(target));
        }
        return serialize(documents);
    }

    private String serialize(List<Document> documents) {
        try {
            return mapper.writeValueAsString(documents);
        } catch (IOException e) {
            LOGGER.error("unable serialize documents");
            throw new RuntimeException(e);
        }
    }
}
