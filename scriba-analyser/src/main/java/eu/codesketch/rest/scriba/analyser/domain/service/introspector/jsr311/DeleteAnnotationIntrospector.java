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
package eu.codesketch.rest.scriba.analyser.domain.service.introspector.jsr311;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;

import eu.codesketch.rest.scriba.analyser.domain.model.decorator.Decorator;
import eu.codesketch.rest.scriba.analyser.domain.model.document.DocumentBuilder;
import eu.codesketch.rest.scriba.analyser.domain.service.introspector.Introspector;

/**
 * Introspect {@link GET} annotation and populate the provided
 * {@link DocumentBuilder}.
 *
 * @author quirino.brizi
 * @since 29 Jan 2015
 *
 */
public class DeleteAnnotationIntrospector implements Introspector {

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.codesketch.rest.scriba.analyser.introspector.Introspector#instrospect
     * (eu.codesketch.rest.scriba.analyser.builder.DocumentBuilder,
     * java.lang.Object, int)
     */
    @Override
    public void instrospect(DocumentBuilder documentBuilder, Decorator annotation) {
        documentBuilder.setHttpMethod(HttpMethod.DELETE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.codesketch.rest.scriba.analyser.introspector.Introspector#type()
     */
    @Override
    public Class<DELETE> type() {
        return DELETE.class;
    }

}