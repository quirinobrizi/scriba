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
package eu.codesketch.rest.scriba.analyser.infrastructure.guice;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

import java.util.Arrays;
import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import eu.codesketch.rest.scriba.analyser.application.AnalyserService;
import eu.codesketch.rest.scriba.analyser.application.impl.AnalyserServiceImpl;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.Introspector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.IntrospectorManager;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.impl.IntrospectorManagerImpl;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jackson.JsonPropertyAnnotationIntrospector;
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
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jsr349.PastAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jsr349.PatternAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.jsr349.SizeAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.scriba.ApiDescriptionAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.scriba.ApiNameAnnotationIntrospector;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.scriba.BodyTypeAnnotationIntrospector;

/**
 * Injector configuration.
 *
 * @author quirino.brizi
 * @since 2 Feb 2015
 *
 */
public class ScribaInjector extends AbstractModule {

    /**
     * The introspectors used by this {@link AnalyserService} implementation.
     */
    // @formatter:off
    private static final List<Class<? extends Introspector>> INTROSPECTORS = Arrays.asList( 
            // JSR 311
            PathAnnotationIntrospector.class,GetAnnotationIntrospector.class, 
            PostAnnotationIntrospector.class, PutAnnotationIntrospector.class,
            DeleteAnnotationIntrospector.class, HeadAnnotationIntrospector.class,
            OptionsAnnotationIntrospector.class, ConsumesAnnotationIntrospector.class,
            ProducesAnnotationIntrospector.class, PathParamAnnotationIntrospector.class,
            FormParamAnnotationIntrospector.class, QueryParamAnnotationIntrospector.class,
            // Internal
            BodyTypeAnnotationIntrospector.class, 
            // Custom
            ApiDescriptionAnnotationIntrospector.class, ApiNameAnnotationIntrospector.class, 
            // JSR 349
            SizeAnnotationIntrospector.class, PatternAnnotationIntrospector.class, 
            PastAnnotationIntrospector.class, NotNullAnnotationIntrospector.class,
            // Jackson
            JsonPropertyAnnotationIntrospector.class
        );
    // @formatter:on

    @Override
    protected void configure() {
        bind(AnalyserService.class).to(AnalyserServiceImpl.class);
        bind(IntrospectorManager.class).to(IntrospectorManagerImpl.class);
        configureIntrospectors();
    }

    private void configureIntrospectors() {
        Multibinder<Introspector> multibinder = newSetBinder(binder(), Introspector.class);
        for (Class<? extends Introspector> introspector : INTROSPECTORS) {
            multibinder.addBinding().to(introspector);
        }
    }
}
