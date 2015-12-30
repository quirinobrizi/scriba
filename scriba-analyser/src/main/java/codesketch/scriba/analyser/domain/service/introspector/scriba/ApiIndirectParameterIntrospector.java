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

package codesketch.scriba.analyser.domain.service.introspector.scriba;

import codesketch.scriba.analyser.domain.model.Property;
import codesketch.scriba.analyser.domain.model.constraint.RegexContraint;
import codesketch.scriba.analyser.domain.model.decorator.Descriptor;
import codesketch.scriba.analyser.domain.model.document.DocumentBuilder;
import codesketch.scriba.analyser.domain.service.introspector.Introspector;
import codesketch.scriba.analyser.domain.service.introspector.IntrospectorHelper;
import codesketch.scriba.annotations.ApiIndirectParameter;
import codesketch.scriba.annotations.ApiParameter;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.Arrays;

import static codesketch.scriba.analyser.domain.service.introspector.IntrospectorHelper.getDefaultValueIfAny;

/**
 * Introspect {@link ApiIndirectParameter} annotations.
 * <p/>
 * Created by quirino on 25/12/15.
 */
@Singleton
public class ApiIndirectParameterIntrospector implements Introspector {
    @Override
    public void instrospect(DocumentBuilder documentBuilder, Descriptor descriptor) {
        ApiIndirectParameter apiIndirectParameter = descriptor.getWrappedAnnotationAs(ApiIndirectParameter.class);
        ApiIndirectParameter.Type type = apiIndirectParameter.type();
        String parameterType = apiIndirectParameter.objectType().getName();
        String defaultValue = apiIndirectParameter.defaultValue();
        Property property = new Property(parameterType, apiIndirectParameter.value(), defaultValue, apiIndirectParameter.description());
        property.addConstraint(new RegexContraint(apiIndirectParameter.constraint(), new ArrayList<String>()));
        switch (type) {
            case COOKIE:
                documentBuilder.putCookieParameter(descriptor.annotatedElement(), property);
                break;
            case FORM:
                documentBuilder.putFormParameter(descriptor.annotatedElement(), property);
                break;
            case HEADER:
                documentBuilder.putHeaderParameter(descriptor.annotatedElement(), property);
                break;
            case PATH:
                documentBuilder.putPathParameter(descriptor.annotatedElement(), property);
                break;
            case QUERY:
                documentBuilder.putQueryParameter(descriptor.annotatedElement(), property);
                break;
        }
    }

    @Override
    public Class<?> type() {
        return ApiIndirectParameter.class;
    }
}
