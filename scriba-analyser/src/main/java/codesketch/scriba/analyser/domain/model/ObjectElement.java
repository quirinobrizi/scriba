package codesketch.scriba.analyser.domain.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quirino.brizi on 22/08/15.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public abstract class ObjectElement {

    @JsonProperty private String name;
    @JsonProperty private String defaultValue;
    @JsonProperty private Boolean nullable;
    @JsonProperty private List<String> constraints;

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

    public ObjectElement constraints(String constraints) {
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

    public List<String> getConstraints() {
        return constraints;
    }
}
