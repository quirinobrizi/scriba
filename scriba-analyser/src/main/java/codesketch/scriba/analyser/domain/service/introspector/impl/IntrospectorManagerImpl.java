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
package codesketch.scriba.analyser.domain.service.introspector.impl;

import codesketch.scriba.analyser.domain.service.introspector.Introspector;
import codesketch.scriba.analyser.domain.service.introspector.IntrospectorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A manager for introspectors, allows to retrieve introspectors via the
 * declared type.
 *
 * @author quirino.brizi
 * @since 29 Jan 2015
 *
 */
@Resource
public class IntrospectorManagerImpl implements IntrospectorManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntrospectorManagerImpl.class);

    private final Map<Class<?>, Introspector> introspectors = new HashMap<>();

    @Inject
    public IntrospectorManagerImpl(Set<Introspector> introspectors) {
        for (Introspector introspector : introspectors) {
            this.register(introspector);
        }
    }

    /* (non-Javadoc)
     * @see eu.codesketch.rest.scriba.analyser.domain.service.introspector.IntrospectorManagerInt#introspector(java.lang.Class)
     */
    @Override
    public Introspector introspector(Class<?> type) {
        if (this.introspectors.containsKey(type)) {
            return this.introspectors.get(type);
        }
        LOGGER.info("unable lookup introspector for requested type {}", type);
        return null;
    }

    /* (non-Javadoc)
     * @see eu.codesketch.rest.scriba.analyser.domain.service.introspector.IntrospectorManagerInt#register(eu.codesketch.rest.scriba.analyser.domain.service.introspector.Introspector)
     */
    @Override
    public void register(Introspector introspector) {
        this.introspectors.put(introspector.type(), introspector);
    }
}
