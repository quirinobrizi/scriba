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
        super(name, defaultValue, true);
        this.type = normalizeParameterType(type);
        this.properties = new ArrayList<>();
    }

    public void addProperty(Property property) {
        this.properties.add(property);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Parameter [type=").append(type).append(", name=").append(getName())
                .append(", defaultValue=").append(getDefaultValue()).append("]");
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
