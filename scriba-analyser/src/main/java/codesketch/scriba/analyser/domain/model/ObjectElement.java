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

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import codesketch.scriba.analyser.domain.model.constraint.Constraint;

/**
 * Created by quirino.brizi on 22/08/15.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public abstract class ObjectElement {

    @JsonProperty private String name;
    @JsonProperty private String defaultValue;
    @JsonProperty private Boolean nullable;
    @JsonProperty private List<Constraint> constraints;

    protected ObjectElement(String name, String defaultValue, Boolean nullable) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.nullable = nullable;
        this.constraints = new ArrayList<>();
    }

    public ObjectElement nullable(Boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public ObjectElement constraints(Constraint constraints) {
        this.constraints.add(constraints);
        return this;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getName() {
        return name;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }
}
