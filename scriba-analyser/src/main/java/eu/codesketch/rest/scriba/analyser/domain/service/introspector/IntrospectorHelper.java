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
package eu.codesketch.rest.scriba.analyser.domain.service.introspector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.codesketch.rest.scriba.analyser.domain.model.decorator.Descriptor;
import eu.codesketch.rest.scriba.analyser.domain.model.document.DocumentBuilder;

/**
 * A simple helper class that allow to reduce introspector related code
 * duplication.
 *
 * @author quirino.brizi
 * @since 3 Feb 2015
 *
 */
public abstract class IntrospectorHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntrospectorHelper.class);

    private IntrospectorHelper() {
    }

    /**
     * Execute introspection based on the provided descriptor.
     * 
     * @param introspectorManager
     *            the introspector manager
     * @param documentBuilder
     *            the document builder to pass to the target
     *            {@link Introspector}
     * @param descriptor
     *            the introspection descriptor.
     */
    public static void introspect(IntrospectorManager introspectorManager,
                    DocumentBuilder documentBuilder, Descriptor descriptor) {
        Introspector introspector = introspectorManager.introspector(descriptor.annotationType());
        if (null != introspector) {
            introspector.instrospect(documentBuilder, descriptor);
        } else {
            LOGGER.warn("unable instrospect {} as no valid introspector has been found", descriptor);
        }
    }
}
