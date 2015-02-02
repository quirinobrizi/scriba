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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 * @author quirino.brizi
 * @since 29 Jan 2015
 *
 */
public class IntrospectorManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntrospectorManager.class);

    private final Map<Class<?>, Introspector> introspectors = new HashMap<>();

    public Introspector introspector(Class<?> type) {
        if (this.introspectors.containsKey(type)) {
            return this.introspectors.get(type);
        }
        LOGGER.info("unable lookup introspector for requested type {}", type);
        return null;
    }

    public void register(Introspector introspector) {
        this.introspectors.put(introspector.type(), introspector);
    }
}
