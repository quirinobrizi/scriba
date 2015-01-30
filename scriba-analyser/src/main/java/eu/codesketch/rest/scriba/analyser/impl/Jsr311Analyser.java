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
package eu.codesketch.rest.scriba.analyser.impl;

import static eu.codesketch.rest.scriba.analyser.helper.ReflectionHelper.getAnnotatedMethods;
import static eu.codesketch.rest.scriba.analyser.helper.ReflectionHelper.getDecorators;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.codesketch.rest.scriba.analyser.Analyser;
import eu.codesketch.rest.scriba.analyser.builder.DocumentBuilder;
import eu.codesketch.rest.scriba.analyser.introspector.Introspector;
import eu.codesketch.rest.scriba.analyser.introspector.IntrospectorManager;
import eu.codesketch.rest.scriba.analyser.introspector.impl.ApiDescriptionAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.introspector.impl.ApiNameAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.introspector.impl.BodyTypeAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.introspector.impl.ConsumesAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.introspector.impl.DeleteAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.introspector.impl.FormParamAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.introspector.impl.GetAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.introspector.impl.HeadAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.introspector.impl.OptionsAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.introspector.impl.PathAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.introspector.impl.PathParamAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.introspector.impl.PostAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.introspector.impl.ProducesAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.introspector.impl.PutAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.introspector.impl.QueryParamAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.model.Decorator;
import eu.codesketch.rest.scriba.analyser.model.Document;

/**
 * Analyse interfaces that defines REST APIs using <a
 * href="https://jcp.org/aboutJava/communityprocess/final/jsr311/">JSR-311</a>
 * annotations.
 *
 * @author quirino.brizi
 * @since 28 Jan 2015
 *
 */
public class Jsr311Analyser implements Analyser {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jsr311Analyser.class);

    /**
     * The introspectors used by this {@link Analyser} implementation.
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
            new ApiNameAnnotationIntrospector() };
    // @formatter:on

    private IntrospectorManager introspectorManager;

    public Jsr311Analyser() {
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
            LOGGER.warn("uanble instrospect {} as no valid introspector has been found", annotation);
        }
    }
}
