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
package codesketch.scriba.analyser.domain.model;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Represent an api payload
 *
 * @author quirino.brizi
 * @since 29 Jan 2015
 *
 */
public class Payload {

    private Map<AnnotatedElement, Property> properties = new HashMap<>();

    public Payload addParameter(AnnotatedElement annotatedElement, Property parameter) {
        if (!this.properties.containsValue(parameter)) {
            this.properties.put(annotatedElement, parameter);
        }
        return this;
    }

    @JsonProperty
    public List<Property> getProperties() {
        return new ArrayList<>(properties.values());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Payload [properties=").append(properties).append("]");
        return builder.toString();
    }

    @JsonIgnore
    public Boolean hasProperty(AnnotatedElement annotatedElement) {
        return this.properties.containsKey(annotatedElement);
    }

    @JsonIgnore
    public Property getProperty(AnnotatedElement annotatedElement) {
        return this.properties.get(annotatedElement);
    }
}
