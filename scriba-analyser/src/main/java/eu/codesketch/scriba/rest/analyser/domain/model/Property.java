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
package eu.codesketch.scriba.rest.analyser.domain.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * Representation of a method parameter
 *
 * @author quirino.brizi
 * @since 29 Jan 2015
 *
 */
@JsonSerialize(include = Inclusion.NON_EMPTY)
public class Property {

    @JsonProperty private String type;
    @JsonProperty private String name;
    @JsonProperty private String defaultValue;
    @JsonProperty private Boolean nullable;
    @JsonProperty private List<String> constraints;
    @JsonProperty private List<Property> properties;

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
        this.type = normalizeParameterType(type);
        this.name = name;
        this.defaultValue = defaultValue;
        this.nullable = Boolean.TRUE;
        this.constraints = new ArrayList<>();
        this.properties = new ArrayList<>();
    }

    public Property nullable(Boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public Property constraints(String constraints) {
        this.constraints.add(constraints);
        return this;
    }

    public void addProperty(Property property) {
        this.properties.add(property);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Parameter [type=").append(type).append(", name=").append(name)
                        .append(", defaultValue=").append(defaultValue).append("]");
        return builder.toString();
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
        return new EqualsBuilder().append(this.type, other.type).append(this.name, other.name)
                        .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.type).append(this.name).build();
    }

    private String normalizeParameterType(String parameterType) {
        if (null == parameterType) {
            return null;
        }
        return parameterType.substring(parameterType.lastIndexOf('.') + 1, parameterType.length())
                        .toLowerCase();
    }
}
