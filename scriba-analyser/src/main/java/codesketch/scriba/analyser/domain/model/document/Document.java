
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

package codesketch.scriba.analyser.domain.model.document;

import static codesketch.scriba.analyser.domain.model.HttpMethods.lookupHttpMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import codesketch.scriba.analyser.domain.model.Description;
import codesketch.scriba.analyser.domain.model.HttpMethods;
import codesketch.scriba.analyser.domain.model.Message;
import codesketch.scriba.analyser.domain.model.Name;
import codesketch.scriba.analyser.domain.model.Path;
import codesketch.scriba.analyser.domain.model.Payload;
import codesketch.scriba.analyser.domain.model.Property;

/**
 * Represent the documentation information about an API.
 *
 * @author quirino.brizi
 * @since 28 Jan 2015
 *
 */
@JsonSerialize(include = Inclusion.NON_EMPTY)
public class Document {

    private static final String PATH_PARAMETER_KEY = "path";
    private static final String HEADER_PARAMETER_KEY = "header";
    private static final String QUERY_PARAMETER_KEY = "query";
    private static final String FORM_PARAMETER_KEY = "form";
    private static final String REQUEST_PAYLOAD_KEY = "request";
    private static final String RESPONSE_PAYLOAD_KEY = "response";

    private HttpMethods httpMethod;
    private Name name;
    private Path path;
    private Description description;
    
    /**
     * The scriba specification version being used.
     */
    @JsonProperty private String scriba = "1.0";
    @JsonProperty private List<String> consumes;
    @JsonProperty private List<String> produces;
    @JsonProperty private Map<String, List<Property>> parameters;
    @JsonProperty private Map<String, List<Payload>> payloads;
    @JsonProperty private List<Message> messages;

    private Document(String httpMethod) {
        this.httpMethod = lookupHttpMethod(httpMethod);
        this.parameters = new HashMap<String, List<Property>>();
        this.payloads = new HashMap<String, List<Payload>>();
    }

    public static Document createNewDocument(String httpMethod) {
        return new Document(httpMethod);
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
        this.parameters.put(PATH_PARAMETER_KEY,
                        null == pathParameters ? new ArrayList<Property>() : pathParameters);
        return this;
    }

    public Document withFormParameters(List<Property> formParameters) {
        this.parameters.put(FORM_PARAMETER_KEY,
                        null == formParameters ? new ArrayList<Property>() : formParameters);
        return this;
    }

    public Document withQueryParameters(List<Property> queryParameters) {
        this.parameters.put(QUERY_PARAMETER_KEY,
                        null == queryParameters ? new ArrayList<Property>() : queryParameters);
        return this;
    }

    public Document withHeaderParameters(List<Property> headerParameters) {
        this.parameters.put(HEADER_PARAMETER_KEY,
                        null == headerParameters ? new ArrayList<Property>() : headerParameters);
        return this;
    }

    public Document withRequestPayload(Payload payload) {
        this.payloads.put(REQUEST_PAYLOAD_KEY,
                        null == payload ? new ArrayList<Payload>() : Arrays.asList(payload));
        return this;
    }

    public Document withResponsePayloads(List<Payload> responsePayloads) {
        this.payloads.put(RESPONSE_PAYLOAD_KEY,
                        null == responsePayloads ? new ArrayList<Payload>() : responsePayloads);
        return this;
    }

    public Document withMessages(List<Message> messages) {
        this.messages = messages;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Document [httpMethod=").append(httpMethod).append(", name=").append(name)
                        .append(", description=").append(description).append(", path=").append(path)
                        .append(", consumes=").append(consumes).append(", produces=")
                        .append(produces).append(", parameters=").append(parameters)
                        .append(", payloads=").append(payloads).append("]");
        return builder.toString();
    }

}
