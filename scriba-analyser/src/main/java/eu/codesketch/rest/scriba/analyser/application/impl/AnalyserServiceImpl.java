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
package eu.codesketch.rest.scriba.analyser.application.impl;

import static eu.codesketch.rest.scriba.analyser.domain.model.decorator.Decorator.decoratorOrderComparator;
import static eu.codesketch.rest.scriba.analyser.infrastructure.helper.ReflectionHelper.getAnnotatedMethods;
import static eu.codesketch.rest.scriba.analyser.infrastructure.helper.ReflectionHelper.getDecorators;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.codesketch.rest.scriba.analyser.application.AnalyserService;
import eu.codesketch.rest.scriba.analyser.domain.model.decorator.Decorator;
import eu.codesketch.rest.scriba.analyser.domain.model.document.Document;
import eu.codesketch.rest.scriba.analyser.domain.model.document.DocumentBuilder;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.Introspector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.IntrospectorManager;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jsr311.ConsumesAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jsr311.DeleteAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jsr311.FormParamAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jsr311.GetAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jsr311.HeadAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jsr311.OptionsAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jsr311.PathAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jsr311.PathParamAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jsr311.PostAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jsr311.ProducesAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jsr311.PutAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jsr311.QueryParamAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jsr349.NotNullAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jsr349.PatternAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jsr349.SizeAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.scriba.ApiDescriptionAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.scriba.ApiNameAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.scriba.BodyTypeAnnotationIntrospector;
import eu.codesketch.rest.scriba.annotations.ApiDescription;
import eu.codesketch.rest.scriba.annotations.ApiName;

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
public class AnalyserServiceImpl implements AnalyserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyserServiceImpl.class);

    /**
     * The introspectors used by this {@link AnalyserService} implementation.
     */
    // @formatter:off
    private static final Introspector[] INTROSPECTORS = new Introspector[] { 
            new PathAnnotationIntrospector(),new GetAnnotationIntrospector(), 
            new PostAnnotationIntrospector(), new PutAnnotationIntrospector(),
            new DeleteAnnotationIntrospector(), new HeadAnnotationIntrospector(),
            new OptionsAnnotationIntrospector(), new ConsumesAnnotationIntrospector(),
            new ProducesAnnotationIntrospector(), new PathParamAnnotationIntrospector(),
            new FormParamAnnotationIntrospector(), new QueryParamAnnotationIntrospector(),
            new BodyTypeAnnotationIntrospector(), new ApiDescriptionAnnotationIntrospector(), 
            new ApiNameAnnotationIntrospector(), new NotNullAnnotationIntrospector(), 
            new SizeAnnotationIntrospector(), new PatternAnnotationIntrospector() };
    // @formatter:on

    private IntrospectorManager introspectorManager;

    public AnalyserServiceImpl() {
        this.introspectorManager = new IntrospectorManager();
        for (Introspector introspector : INTROSPECTORS) {
            this.introspectorManager.register(introspector);
        }
    }

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
        List<DocumentBuilder> builders = collectAlldeclaredAnnotations(clazz);

        for (DocumentBuilder documentBuilder : builders) {
            documents.add(documentBuilder.build());
        }

        LOGGER.info("document built {}", documents);
        return documents;
    }

    private List<DocumentBuilder> collectAlldeclaredAnnotations(Class<?> clazz) {
        List<DocumentBuilder> builders = new ArrayList<>();

        List<Decorator> typeDecorators = getDecorators(clazz);
        LOGGER.debug("retrieved type annotations {}", typeDecorators);
        DocumentBuilder documentBuilder = new DocumentBuilder();
        int level = 0;
        for (Decorator decorator : typeDecorators) {
            introspect(documentBuilder, decorator);
            level = decorator.level();
        }

        Set<Method> methods = getAnnotatedMethods(clazz);
        LOGGER.debug("retrieved methods {}", methods);
        level += 1;
        for (Method method : methods) {
            List<Decorator> decorators = getDecorators(method, level);
            Collections.sort(decorators, decoratorOrderComparator());
            DocumentBuilder documentBuilderClone = documentBuilder.clone();
            for (Decorator annotation : decorators) {
                introspect(documentBuilderClone, annotation);
            }
            builders.add(documentBuilderClone);
        }

        LOGGER.debug("available document builders {}", builders);
        return builders;
    }

    private void introspect(DocumentBuilder documentBuilder, Decorator annotation) {
        Introspector introspector = this.introspectorManager.introspector(annotation
                        .annotationType());
        if (null != introspector) {
            introspector.instrospect(documentBuilder, annotation);
        } else {
            LOGGER.warn("unable instrospect {} as no valid introspector has been found", annotation);
        }
    }
}
