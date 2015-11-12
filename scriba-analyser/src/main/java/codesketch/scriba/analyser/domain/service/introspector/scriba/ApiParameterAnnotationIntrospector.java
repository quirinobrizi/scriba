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
package codesketch.scriba.analyser.domain.service.introspector.scriba;

import codesketch.scriba.analyser.domain.model.Property;
import codesketch.scriba.analyser.domain.model.decorator.Descriptor;
import codesketch.scriba.analyser.domain.model.document.DocumentBuilder;
import codesketch.scriba.analyser.domain.service.introspector.Introspector;
import codesketch.scriba.annotations.ApiDefault;
import codesketch.scriba.annotations.ApiParameter;
import codesketch.scriba.annotations.ApiParameter.Type;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;

/**
 * Introspect {@link Consumes} annotation and populate the provided
 * {@link DocumentBuilder}.
 *
 * While populating the {@link DocumentBuilder} this introspector will take into
 * account that method level annotations will override the class level one.
 *
 * @author quirino.brizi
 * @since 29 Jan 2015
 *
 */
@Singleton
public class ApiParameterAnnotationIntrospector implements Introspector {

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.codesketch.rest.scriba.analyser.introspector.Introspector#instrospect
     * (eu.codesketch.rest.scriba.analyser.builder.DocumentBuilder,
     * java.lang.Object, int)
     */
    @Override
    public void instrospect(DocumentBuilder documentBuilder, Descriptor decorator) {
        ApiParameter apiParameter = decorator.getWrappedAnnotationAs(ApiParameter.class);
        Type type = apiParameter.type();
        String parameterType = decorator.getParameterType().getName();
        String defaultValue = getDefaultValueIfAny(decorator);
        switch (type) {
        case COOKIE:
            documentBuilder.putCookieParameter(decorator.annotatedElement(),
                            new Property(parameterType, apiParameter.value(), defaultValue));
            break;
        case FORM:
            documentBuilder.putFormParameter(decorator.annotatedElement(),
                            new Property(parameterType, apiParameter.value(), defaultValue));
            break;
        case HEADER:
            documentBuilder.putHeaderParameter(decorator.annotatedElement(),
                            new Property(parameterType, apiParameter.value(), defaultValue));
            break;
        case PATH:
            documentBuilder.putPathParameter(decorator.annotatedElement(),
                            new Property(parameterType, apiParameter.value(), defaultValue));
            break;
        case QUERY:
            documentBuilder.putQueryParameter(decorator.annotatedElement(),
                            new Property(parameterType, apiParameter.value(), defaultValue));
            break;
        }
    }

    private String getDefaultValueIfAny(Descriptor decorator) {
        codesketch.scriba.analyser.infrastructure.reflect.Parameter parameter = decorator
                        .annotatedElementAs(
                                        codesketch.scriba.analyser.infrastructure.reflect.Parameter.class);
        DefaultValue defaultValueAnnotation = parameter.getAnnotation(DefaultValue.class);
        if (null == defaultValueAnnotation) {
            ApiDefault apiDefault = parameter.getAnnotation(ApiDefault.class);
            return null != apiDefault ? apiDefault.value() : null;
        } else {
            return defaultValueAnnotation.value();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.codesketch.rest.scriba.analyser.introspector.Introspector#type()
     */
    @Override
    public Class<ApiParameter> type() {
        return ApiParameter.class;
    }

}
