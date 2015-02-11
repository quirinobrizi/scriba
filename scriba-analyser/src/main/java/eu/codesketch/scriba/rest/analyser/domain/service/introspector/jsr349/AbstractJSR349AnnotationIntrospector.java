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
package eu.codesketch.scriba.rest.analyser.domain.service.introspector.jsr349;

import eu.codesketch.scriba.rest.analyser.domain.model.decorator.Descriptor;
import eu.codesketch.scriba.rest.analyser.domain.service.introspector.Introspector;
import eu.codesketch.scriba.rest.analyser.infrastructure.helper.MessageInterpolatorHelper;

/**
 * 
 *
 * @author quirino.brizi
 * @since 11 Feb 2015
 *
 */
public abstract class AbstractJSR349AnnotationIntrospector implements Introspector {

    private MessageInterpolatorHelper messageInterpolatorHelper = new MessageInterpolatorHelper();

    protected String interpolate(String message, Descriptor descriptor) {
        return messageInterpolatorHelper.interpolate(message, descriptor);
    }
}
