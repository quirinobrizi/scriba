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
package eu.codesketch.rest.scriba.analyser.model;

import static eu.codesketch.rest.scriba.analyser.model.HttpMethods.lookupHttpMethod;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * Represent the documentation information about an API.
 * 
 * @author quirino.brizi
 * @since 28 Jan 2015
 *
 */
@JsonSerialize(include = Inclusion.NON_EMPTY)
public class Document {

    private HttpMethods httpMethod;
    private Name name;
    private Description description;
    private Path path;
    @JsonProperty private List<String> consumes;
    @JsonProperty private List<String> produces;
    @JsonProperty private List<Parameter> pathParameters;
    @JsonProperty private List<Parameter> formParameters;
    @JsonProperty private List<Parameter> queryParameters;
    @JsonProperty private Payload payload;

    private Document(String httpMethod) {
        this.httpMethod = lookupHttpMethod(httpMethod);
    }

    @JsonProperty
    public String getHttpMethod() {
        return httpMethod.getMethod();
    }

    @JsonProperty
    public String getName() {
        return null != name ? name.toString() : null;
    }

    @JsonProperty
    public String getDescription() {
        return null != description ? description.toString() : null;
    }

    @JsonProperty
    public String getPath() {
        return path.toString();
    }

    public Document withName(Name name) {
        this.name = name;
        return this;
    }

    public Document withPath(Path path) {
        this.path = path;
        return this;
    }

    public Document withDescription(Description description) {
        this.description = description;
        return this;
    }

    public Document withConsumes(List<String> consumables) {
        if (this.httpMethod.hasPayload()) {
            this.consumes = consumables;
        }
        return this;
    }

    public Document withProduces(List<String> producible) {
        this.produces = producible;
        return this;
    }

    public Document withPathParameters(List<Parameter> pathParameters) {
        this.pathParameters = pathParameters;
        return this;
    }

    public Document withFormParameters(List<Parameter> formParameters) {
        this.formParameters = formParameters;
        return this;
    }

    public Document withQueryParameters(List<Parameter> queryParameters) {
        this.queryParameters = queryParameters;
        return this;
    }

    public Document withPayload(Payload payload) {
        this.payload = payload;
        return this;
    }

    public static Document createNewDocument(String httpMethod) {
        return new Document(httpMethod);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Document [httpMethod=").append(httpMethod).append(", name=").append(name)
                        .append(", description=").append(description).append(", path=")
                        .append(path).append(", consumes=").append(consumes).append(", produces=")
                        .append(produces).append(", pathParameters=").append(pathParameters)
                        .append(", payload=").append(payload).append("]");
        return builder.toString();
    }
}
