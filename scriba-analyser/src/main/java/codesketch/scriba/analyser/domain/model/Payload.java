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
package codesketch.scriba.analyser.domain.model;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Represent an api payload
 *
 * @author quirino.brizi
 * @since 29 Jan 2015
 *
 */
public class Payload extends ObjectElement {

    @JsonProperty private Class<?> type;

    private Map<AnnotatedElement, Property> properties = new HashMap<>();

    public Payload(Class<?> type, String name) {
        super(null, "", false);
        Validate.notNull(type, "Payload type must be provided");
        this.type = type;
    }

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

    @JsonIgnore
    public boolean isOfType(Class<?> type) {
        System.out.println(this.type + " -- " + type);
        return this.type.equals(type);
    }
}
