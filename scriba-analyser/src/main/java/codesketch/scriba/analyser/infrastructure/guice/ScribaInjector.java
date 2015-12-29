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
package codesketch.scriba.analyser.infrastructure.guice;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

import java.util.Arrays;
import java.util.List;

import codesketch.scriba.analyser.domain.service.introspector.scriba.*;
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
            ApiVerbAnnotationIntrospector.class, ApiIndirectParameterIntrospector.class,
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
