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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a method parameter
 *
 * @author quirino.brizi
 * @since 29 Jan 2015
 *
 */
@JsonSerialize(include = Inclusion.NON_EMPTY)
public class Property extends ObjectElement {

    @JsonProperty private String type;
    @JsonProperty private List<Property> properties;
    @JsonProperty private String description;

    /**
     * Create a new parameter setting its type and name;
     *
     * @param type
     *            the parameter type
     * @param name
     *            the parameter name
     */
    public Property(String type, String name) {
        this(type, name, null);
    }

    public Property(String type, String name, String defaultValue) {
        this(type, name, defaultValue, null);
    }

    public Property(String type, String name, String defaultValue, String description) {
        super(name, defaultValue, true);
        this.type = normalizeParameterType(type);
        this.properties = new ArrayList<>();
        this.description = description;
    }

    public void addProperty(Property property) {
        this.properties.add(property);
    }

    @Override
    public String toString() {
        return "Parameter [type=" + type + ", name=" + getName() +
                ", defaultValue=" + getDefaultValue() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        Property other = (Property) obj;
        return new EqualsBuilder().append(this.type, other.type).append(getName(), other.getName())
                        .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.type).append(getName()).build();
    }

    private String normalizeParameterType(String parameterType) {
        if (null == parameterType) {
            return null;
        }
        return parameterType.substring(parameterType.lastIndexOf('.') + 1, parameterType.length())
                        .toLowerCase();
    }
}
