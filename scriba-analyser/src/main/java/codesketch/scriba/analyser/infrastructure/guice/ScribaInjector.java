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
package codesketch.scriba.analyser.infrastructure.guice;

import codesketch.scriba.analyser.application.AnalyserService;
import codesketch.scriba.analyser.application.impl.AnalyserServiceImpl;
import codesketch.scriba.analyser.domain.service.introspector.Introspector;
import codesketch.scriba.analyser.domain.service.introspector.IntrospectorManager;
import codesketch.scriba.analyser.domain.service.introspector.impl.IntrospectorManagerImpl;
import codesketch.scriba.analyser.domain.service.introspector.jackson.JsonPropertyAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr311.*;
import codesketch.scriba.analyser.domain.service.introspector.jsr349.*;
import codesketch.scriba.analyser.domain.service.introspector.scriba.*;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import java.util.Arrays;
import java.util.List;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

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
            RequestPayloadAnnotationIntrospector.class, 
            // Custom
            ApiDescriptionAnnotationIntrospector.class, ApiNameAnnotationIntrospector.class, 
            ApiResponseAnnotationIntrospector.class, ApiVerbAnnotationIntrospector.class,
            ApiConsumesAnnotationIntrospector.class, ApiProducesAnnotationIntrospector.class,
            ApiParameterAnnotationIntrospector.class, ApiPathAnnotationIntrospector.class,
            // JSR 349
            AssertFalseAnnotationIntrospector.class, AssertTrueAnnotationIntrospector.class,
            DecimalMaxAnnotationIntrospector.class, DecimalMinAnnotationIntrospector.class,
            DigitsAnnotationIntrospector.class, FutureAnnotationIntrospector.class,
            MaxAnnotationIntrospector.class, MinAnnotationIntrospector.class,
            NotNullAnnotationIntrospector.class, NullAnnotationIntrospector.class,
            PastAnnotationIntrospector.class, PatternAnnotationIntrospector.class,
            SizeAnnotationIntrospector.class, ValidAnnotationIntrospector.class,
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
