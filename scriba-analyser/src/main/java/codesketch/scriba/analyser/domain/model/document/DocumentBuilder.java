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
package codesketch.scriba.analyser.domain.model.document;

import static codesketch.scriba.analyser.domain.model.document.Document.createNewDocument;
import static codesketch.scriba.analyser.infrastructure.helper.StringHelper.join;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import codesketch.scriba.analyser.domain.model.Description;
import codesketch.scriba.analyser.domain.model.Message;
import codesketch.scriba.analyser.domain.model.Name;
import codesketch.scriba.analyser.domain.model.Path;
import codesketch.scriba.analyser.domain.model.Payload;
import codesketch.scriba.analyser.domain.model.Property;

/**
 * A builder for {@link Document} class.
 *
 * @author quirino.brizi
 * @since 29 Jan 2015
 *
 */
public class DocumentBuilder implements Cloneable {

    private String httpMethod;
    private List<String> pathSegments;
    private Map<AnnotatedElement, Property> cookieParameters;
    private Map<AnnotatedElement, Property> headerParameters;
    private Map<AnnotatedElement, Property> pathParameters;
    private Map<AnnotatedElement, Property> formParameters;
    private Map<AnnotatedElement, Property> queryParameters;
    private List<String> consumables;
    private List<String> producible;
    private Set<Message> messages;
    private Payload requestPayload;
    private Payload responsePayload;
    private Name name;
    private Description description;

    public DocumentBuilder() {
        this.pathSegments = new LinkedList<>();
        this.cookieParameters = new HashMap<>();
        this.headerParameters = new HashMap<>();
        this.pathParameters = new HashMap<>();
        this.formParameters = new HashMap<>();
        this.queryParameters = new HashMap<>();
        this.consumables = new ArrayList<>();
        this.producible = new ArrayList<>();
        this.messages = new HashSet<>();
        this.requestPayload = new Payload();
        this.responsePayload = new Payload();
    }

    public DocumentBuilder setName(Name name) {
        this.name = name;
        return this;
    }

    public DocumentBuilder setDescription(Description description) {
        this.description = description;
        return this;
    }

    public DocumentBuilder addPathSegment(String value, int level) {
        this.pathSegments.add(level, value);
        return this;
    }

    public DocumentBuilder setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public DocumentBuilder putCookieParameter(AnnotatedElement annotatedElement,
                    Property pathParameter) {
        this.cookieParameters.put(annotatedElement, pathParameter);
        return this;
    }

    public DocumentBuilder putHeaderParameter(AnnotatedElement annotatedElement, Property parameter) {
        this.headerParameters.put(annotatedElement, parameter);
        return this;
    }

    public DocumentBuilder putPathParameter(AnnotatedElement annotatedElement,
                    Property pathParameter) {
        this.pathParameters.put(annotatedElement, pathParameter);
        return this;
    }

    public DocumentBuilder putFormParameter(AnnotatedElement annotatedElement, Property parameter) {
        this.formParameters.put(annotatedElement, parameter);
        return this;
    }

    public DocumentBuilder putQueryParameter(AnnotatedElement annotatedElement, Property parameter) {
        this.queryParameters.put(annotatedElement, parameter);
        return this;
    }

    /**
     * Verifies if a parameter is present for the annotated element.
     * 
     * @param annotatedElement
     *            the annotated element to check
     * @return true if the a parameter exists for the annotated element as a one
     *         of form, path, or query parameter, false otherwise.
     */
    public Boolean hasParameterForAnnotatedElement(AnnotatedElement annotatedElement) {
        return this.pathParameters.containsKey(annotatedElement)
                        || this.formParameters.containsKey(annotatedElement)
                        || this.queryParameters.containsKey(annotatedElement)
                        || this.requestPayload.hasProperty(annotatedElement)
                        || this.responsePayload.hasProperty(annotatedElement)
                        || this.cookieParameters.containsKey(annotatedElement)
                        || this.headerParameters.containsKey(annotatedElement);
    }

    public Property getParameter(AnnotatedElement annotatedElement) {
        if (hasParameterForAnnotatedElement(annotatedElement)) {
            Property answer = this.pathParameters.get(annotatedElement);
            if (null != answer) {
                return answer;
            }
            answer = this.formParameters.get(annotatedElement);
            if (null != answer) {
                return answer;
            }
            answer = this.queryParameters.get(annotatedElement);
            if (null != answer) {
                return answer;
            }
            answer = this.requestPayload.getProperty(annotatedElement);
            if (null != answer) {
                return answer;
            }
            answer = this.responsePayload.getProperty(annotatedElement);
            if (null != answer) {
                return answer;
            }
            answer = this.cookieParameters.get(annotatedElement);
            if (null != answer) {
                return answer;
            }
            return this.headerParameters.get(annotatedElement);
        }
        throw new IllegalStateException(
                        String.format("requested annotated element %s has not been processed as a parameter, is the API correclty annotated?",
                                        annotatedElement));
    }

    /**
     * Set or replaces consumable types. This is because method level annotation
     * 
     * @param value
     *            the consumable types.
     * @param isMethod
     *            a flag indicating whether the consumable types are defined at
     *            method or type level.
     * @return
     */
    public DocumentBuilder setOrReplaceConsumableTypes(String[] value, Boolean isMethod) {
        if (!isMethod) {
            this.consumables.addAll(Arrays.asList(value));
        } else {
            this.consumables.clear();
            this.consumables.addAll(Arrays.asList(value));
        }
        return this;
    }

    /**
     * Set or replaces producible types. This is because method level annotation
     * 
     * @param value
     *            the consumable types.
     * @param isMethod
     *            a flag indicating whether the consumable types are defined at
     *            method or type level.
     * @return
     */
    public DocumentBuilder setOrReplaceProducibleTypes(String[] value, Boolean isMethod) {
        if (!isMethod) {
            this.producible.addAll(Arrays.asList(value));
        } else {
            this.producible.clear();
            this.producible.addAll(Arrays.asList(value));
        }
        return this;
    }

    public DocumentBuilder addMessage(Message message) {
        this.messages.add(message);
        return this;
    }

    public DocumentBuilder setProducible(List<String> producible) {
        this.producible.addAll(producible);
        return this;
    }

    public DocumentBuilder addPayload(Payload payload) {
        this.requestPayload = payload;
        return this;
    }

    public Payload getOrCreateRequestPayload() {
        if (null == this.requestPayload) {
            this.requestPayload = new Payload();
        }
        return this.requestPayload;
    }

    public Payload getOrCreateResponsePayload() {
        if (null == this.responsePayload) {
            this.responsePayload = new Payload();
        }
        return this.responsePayload;
    }

    public Document build() {
        return createNewDocument(this.httpMethod).withName(this.name).withDescription(description)
                        .withPath(new Path(buildPath())).withConsumes(this.consumables)
                        .withProduces(this.producible)
                        .withPathParameters(new ArrayList<>(this.pathParameters.values()))
                        .withFormParameters(new ArrayList<>(this.formParameters.values()))
                        .withQueryParameters(new ArrayList<>(this.queryParameters.values()))
                        .withRequestPayload(this.requestPayload)
                        .withResponsePayload(this.responsePayload)
                        .withMessages(new ArrayList<>(this.messages));
    }

    @Override
    public DocumentBuilder clone() {
        DocumentBuilder documentBuilder = new DocumentBuilder();
        documentBuilder.setHttpMethod(this.httpMethod);
        documentBuilder.setPathSegments(this.pathSegments);
        documentBuilder.setConsumables(this.consumables);
        documentBuilder.setProducible(this.producible);
        return documentBuilder;
    }

    @Override
    public String toString() {
        return "DocumentBuilder [httpMethod=" + httpMethod + ", pathSegments=" + pathSegments
                        + ", pathParameters=" + pathParameters + ", consumables=" + consumables
                        + ", producible=" + producible + ", payload=" + requestPayload + "]";
    }

    private void setPathSegments(List<String> pathSegments) {
        this.pathSegments.addAll(pathSegments);
    }

    private void setConsumables(List<String> consumables) {
        this.consumables.addAll(consumables);
    }

    private String buildPath() {
        String path = join(pathSegments, "/");
        return path.replace("//", "/");
    }
}
