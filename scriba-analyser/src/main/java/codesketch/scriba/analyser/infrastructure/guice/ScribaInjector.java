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

import static com.google.inject.multibindings.Multibinder.newSetBinder;

import java.util.Arrays;
import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import codesketch.scriba.analyser.application.AnalyserService;
import codesketch.scriba.analyser.application.impl.AnalyserServiceImpl;
import codesketch.scriba.analyser.domain.service.introspector.Introspector;
import codesketch.scriba.analyser.domain.service.introspector.IntrospectorManager;
import codesketch.scriba.analyser.domain.service.introspector.impl.IntrospectorManagerImpl;
import codesketch.scriba.analyser.domain.service.introspector.jackson.FasterXMLJsonPropertyAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jackson.JsonPropertyAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr311.ConsumesAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr311.DeleteAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr311.FormParamAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr311.GetAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr311.HeadAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr311.OptionsAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr311.PathAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr311.PathParamAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr311.PostAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr311.ProducesAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr311.PutAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr311.QueryParamAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr349.AssertFalseAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr349.AssertTrueAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr349.DecimalMaxAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr349.DecimalMinAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr349.DigitsAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr349.FutureAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr349.MaxAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr349.MinAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr349.NotNullAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr349.NullAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr349.PastAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr349.PatternAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr349.SizeAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.jsr349.ValidAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.scriba.ApiConsumesAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.scriba.ApiDescriptionAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.scriba.ApiNameAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.scriba.ApiParameterAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.scriba.ApiPathAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.scriba.ApiProducesAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.scriba.ApiResponseAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.scriba.ApiResponsesAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.scriba.ApiVerbAnnotationIntrospector;
import codesketch.scriba.analyser.domain.service.introspector.scriba.RequestPayloadAnnotationIntrospector;

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
            ApiResponseAnnotationIntrospector.class, ApiResponsesAnnotationIntrospector.class, 
            ApiConsumesAnnotationIntrospector.class, ApiProducesAnnotationIntrospector.class,
            ApiParameterAnnotationIntrospector.class, ApiPathAnnotationIntrospector.class,
            ApiVerbAnnotationIntrospector.class,
            // JSR 349
            AssertFalseAnnotationIntrospector.class, AssertTrueAnnotationIntrospector.class,
            DecimalMaxAnnotationIntrospector.class, DecimalMinAnnotationIntrospector.class,
            DigitsAnnotationIntrospector.class, FutureAnnotationIntrospector.class,
            MaxAnnotationIntrospector.class, MinAnnotationIntrospector.class,
            NotNullAnnotationIntrospector.class, NullAnnotationIntrospector.class,
            PastAnnotationIntrospector.class, PatternAnnotationIntrospector.class,
            SizeAnnotationIntrospector.class, ValidAnnotationIntrospector.class,
            // Jackson
            JsonPropertyAnnotationIntrospector.class, FasterXMLJsonPropertyAnnotationIntrospector.class
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
