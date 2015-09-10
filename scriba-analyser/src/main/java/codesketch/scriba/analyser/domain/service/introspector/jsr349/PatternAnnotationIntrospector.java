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
package codesketch.scriba.analyser.domain.service.introspector.jsr349;

import codesketch.scriba.analyser.domain.model.ObjectElement;
import codesketch.scriba.analyser.domain.model.decorator.Descriptor;
import codesketch.scriba.analyser.domain.model.document.DocumentBuilder;

import javax.inject.Singleton;
import javax.validation.constraints.Pattern;
import javax.ws.rs.Consumes;

import static codesketch.scriba.analyser.domain.model.Message.createMessageForBadRequest;
import static org.apache.commons.lang3.StringUtils.join;

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
public class PatternAnnotationIntrospector extends AbstractJSR349AnnotationIntrospector {

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.codesketch.rest.scriba.analyser.introspector.Introspector#instrospect
     * (eu.codesketch.rest.scriba.analyser.builder.DocumentBuilder,
     * java.lang.Object, int)
     */
    @Override
    public void instrospect(DocumentBuilder documentBuilder, Descriptor descriptor) {
        Pattern annotation = descriptor.getWrappedAnnotationAs(type());
        Pattern pattern = descriptor.getWrappedAnnotationAs(Pattern.class);
        ObjectElement parameter = documentBuilder.getParameter(descriptor);
        if (hasFlags(pattern)) {
            parameter.constraints(String.format(
                    "value must match the specified regular expression %s with flags %s",
                    pattern.regexp(), join(pattern.flags(), ", ")));
        } else {
            parameter.constraints(String.format(
                    "value must match the specified regular expression %s",
                    pattern.regexp()));
        }
        documentBuilder.addMessage(createMessageForBadRequest(interpolate(annotation.message(),
                descriptor)));
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.codesketch.rest.scriba.analyser.introspector.Introspector#type()
     */
    @Override
    public Class<Pattern> type() {
        return Pattern.class;
    }

    private Boolean hasFlags(Pattern pattern) {
        return pattern.flags().length > 0;
    }

}