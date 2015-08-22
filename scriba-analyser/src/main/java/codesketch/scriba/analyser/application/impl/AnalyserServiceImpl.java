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
package codesketch.scriba.analyser.application.impl;

import codesketch.scriba.analyser.application.AnalyserService;
import codesketch.scriba.analyser.domain.model.decorator.Descriptor;
import codesketch.scriba.analyser.domain.model.document.Document;
import codesketch.scriba.analyser.domain.model.document.DocumentBuilder;
import codesketch.scriba.analyser.domain.service.introspector.IntrospectorManager;
import codesketch.scriba.analyser.infrastructure.helper.ReflectionHelper;
import codesketch.scriba.annotations.ApiDescription;
import codesketch.scriba.annotations.ApiName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static codesketch.scriba.analyser.domain.model.decorator.Descriptor.descriptorsOrderComparator;
import static codesketch.scriba.analyser.domain.service.introspector.IntrospectorHelper.introspect;
import static codesketch.scriba.analyser.infrastructure.helper.ReflectionHelper.getAnnotatedMethods;
import static codesketch.scriba.analyser.infrastructure.helper.ReflectionHelper.getDescriptors;

/**
 * Analyse interfaces that defines REST APIs using:
 * <ul>
 * <li><a
 * href="https://jcp.org/aboutJava/communityprocess/final/jsr311/">JSR-311</a></li>
 * <li>Custom API annotations {@link ApiName} {@link ApiDescription}
 * <li><a
 * href="https://jcp.org/aboutJava/communityprocess/final/jsr349/">JSR-349</a></li>
 * </ul>
 * annotations.
 *
 * @author quirino.brizi
 * @since 28 Jan 2015
 *
 */
@Singleton
public class AnalyserServiceImpl implements AnalyserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyserServiceImpl.class);

    @Inject private IntrospectorManager introspectorManager;

    /**
     * Analyse the provided class for annotation that describe the REST APIs
     * using <a
     * href="https://jcp.org/aboutJava/communityprocess/final/jsr311/">JSR
     * -311</a>.
     *
     * @param clazz
     *            the class to analyse
     * @return a list of {@link Document} that describe the APIs contained on
     *         the provided class.
     */
    @Override
    public List<Document> analyse(Class<?> clazz) {
        List<Document> documents = new ArrayList<>();
        List<DocumentBuilder> builders = collectDocumentBuilders(clazz);

        for (DocumentBuilder documentBuilder : builders) {
            documents.add(documentBuilder.build());
        }

        LOGGER.info("document built {}", documents);
        return documents;
    }

    private List<DocumentBuilder> collectDocumentBuilders(Class<?> clazz) {
        List<DocumentBuilder> builders = new ArrayList<>();

        List<Descriptor> typeDecorators = getDescriptors(clazz);
        LOGGER.debug("retrieved type annotations {}", typeDecorators);
        DocumentBuilder documentBuilder = new DocumentBuilder();
        int level = 0;
        for (Descriptor decorator : typeDecorators) {
            introspect(this.introspectorManager, documentBuilder, decorator);
            level = decorator.level();
        }

        Set<Method> methods = getAnnotatedMethods(clazz);
        LOGGER.debug("retrieved methods {}", methods);
        level += 1;
        for (Method method : methods) {
            List<Descriptor> decorators = ReflectionHelper.getDescriptors(method, level);
            Collections.sort(decorators, descriptorsOrderComparator());
            DocumentBuilder documentBuilderClone = documentBuilder.clone();
            for (Descriptor annotation : decorators) {
                introspect(this.introspectorManager, documentBuilderClone, annotation);
            }
            builders.add(documentBuilderClone);
        }

        LOGGER.debug("available document builders {}", builders);
        return builders;
    }
}
