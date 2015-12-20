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

    /*
     * (non-Javadoc)
     * 
     * @see eu.codesketch.rest.scriba.analyser.domain.service.introspector.
     * IntrospectorManagerInt#introspector(java.lang.Class)
     */
    @Override
    public Introspector introspector(Class<?> type) {
        if (this.introspectors.containsKey(type)) {
            return this.introspectors.get(type);
        }
        LOGGER.info("unable lookup introspector for requested type {}", type);
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.codesketch.rest.scriba.analyser.domain.service.introspector.
     * IntrospectorManagerInt#register(eu.codesketch.rest.scriba.analyser.domain
     * .service.introspector.Introspector)
     */
    @Override
    public void register(Introspector introspector) {
        this.introspectors.put(introspector.type(), introspector);
    }
}
