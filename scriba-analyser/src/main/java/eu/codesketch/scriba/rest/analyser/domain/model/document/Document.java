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
package eu.codesketch.scriba.rest.analyser.domain.model.document;

import static eu.codesketch.scriba.rest.analyser.domain.model.HttpMethods.lookupHttpMethod;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import eu.codesketch.scriba.rest.analyser.domain.model.Description;
import eu.codesketch.scriba.rest.analyser.domain.model.HttpMethods;
import eu.codesketch.scriba.rest.analyser.domain.model.Message;
import eu.codesketch.scriba.rest.analyser.domain.model.Name;
import eu.codesketch.scriba.rest.analyser.domain.model.Path;
import eu.codesketch.scriba.rest.analyser.domain.model.Payload;
import eu.codesketch.scriba.rest.analyser.domain.model.Property;

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
    private Path path;
    private Description description;
    @JsonProperty private List<String> consumes;
    @JsonProperty private List<String> produces;
    @JsonProperty private List<Property> pathParameters;
    @JsonProperty private List<Property> formParameters;
    @JsonProperty private List<Property> queryParameters;
    @JsonProperty private Payload requestPayload;
    @JsonProperty private Payload responsePayload;
    @JsonProperty private List<Message> messages;

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

    public Document withPathParameters(List<Property> pathParameters) {
        this.pathParameters = pathParameters;
        return this;
    }

    public Document withFormParameters(List<Property> formParameters) {
        this.formParameters = formParameters;
        return this;
    }

    public Document withQueryParameters(List<Property> queryParameters) {
        this.queryParameters = queryParameters;
        return this;
    }

    public Document withRequestPayload(Payload payload) {
        this.requestPayload = payload;
        return this;
    }

    public Document withResponsePayload(Payload responsePayload) {
        this.responsePayload = responsePayload;
        return this;
    }

    public Document withMessages(List<Message> messages) {
        this.messages = messages;
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
                        .append(", payload=").append(requestPayload).append("]");
        return builder.toString();
    }

}
