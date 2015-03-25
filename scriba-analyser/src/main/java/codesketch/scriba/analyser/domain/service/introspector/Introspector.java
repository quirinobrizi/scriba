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
package codesketch.scriba.analyser.domain.service.introspector;

import codesketch.scriba.analyser.domain.model.decorator.Descriptor;
import codesketch.scriba.analyser.domain.model.document.DocumentBuilder;

/**
 * Archetype for data introspection services.
 *
 * @author quirino.brizi
 * @since 29 Jan 2015
 *
 */
public interface Introspector {

    /**
     * introspect the provided value and populate accordingly the provided
     * {@link DocumentBuilder}.
     * 
     * @param documentBuilder
     *            the builder to populate
     * @param descriptor
     *            the introspect context
     */
    void instrospect(DocumentBuilder documentBuilder, Descriptor descriptor);

    /**
     * Declare the type this introspector is made for.
     * 
     * @return the type the introspector is made for.
     */
    Class<?> type();
}
