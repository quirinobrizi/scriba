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
import codesketch.scriba.analyser.domain.model.ObjectElement;
import codesketch.scriba.analyser.domain.model.Path;
import codesketch.scriba.analyser.domain.model.Payload;
import codesketch.scriba.analyser.domain.model.Property;
import codesketch.scriba.analyser.domain.model.decorator.Descriptor;

/**
 * A builder for {@link Document} class.
 *
 * @author quirino.brizi
 * @since 29 Jan 2015
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
    private List<Payload> responsePayloads;
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
        this.responsePayloads = new ArrayList<>();
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

    public DocumentBuilder putHeaderParameter(AnnotatedElement annotatedElement,
                    Property parameter) {
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

    public DocumentBuilder putQueryParameter(AnnotatedElement annotatedElement,
                    Property parameter) {
        this.queryParameters.put(annotatedElement, parameter);
        return this;
    }

    /**
     * Verifies if a parameter is present for the annotated element.
     *
     * @param descriptor
     *            the annotated element to check
     * @return true if the a parameter exists for the annotated element as a one of form, path, or query parameter, false
     *         otherwise.
     */
    public Boolean hasParameterForAnnotatedElement(Descriptor descriptor) {
        AnnotatedElement element = descriptor.annotatedElement();

        boolean found = this.pathParameters.containsKey(element)
                        || this.formParameters.containsKey(element)
                        || this.queryParameters.containsKey(element)
                        || this.cookieParameters.containsKey(element)
                        || this.headerParameters.containsKey(element);
        if (!found) {
            if (descriptor.isResponseInspected()) {
                if (!found) {
                    for (Payload payload : this.responsePayloads) {
                        return payload.isOfType(element.getClass()) || payload.hasProperty(element);
                    }
                }
            } else {
                if (null != this.requestPayload) {
                    found |= this.requestPayload.isOfType(element.getClass())
                                    || this.requestPayload.hasProperty(element);
                }
            }
        }
        return true;

    }
    
    public ObjectElement getParameter(Descriptor descriptor) {
        AnnotatedElement element = descriptor.annotatedElement();
        Property answer = this.pathParameters.get(element);
        if (null != answer) {
            return answer;
        }
        answer = this.formParameters.get(element);
        if (null != answer) {
            return answer;
        }
        answer = this.queryParameters.get(element);
        if (null != answer) {
            return answer;
        }
        answer = this.cookieParameters.get(element);
        if (null != answer) {
            return answer;
        }
        answer = this.headerParameters.get(element);
        if (null != answer) {
            return answer;
        }
        if (descriptor.isResponseInspected()) {
            for (Payload payload : this.responsePayloads) {
                if (payload.isOfType(descriptor.getParameterType())) {
                    return payload;
                }
                answer = payload.getProperty(element);
                if (null != answer) {
                    return answer;
                }
            }

        } else {
            if (this.requestPayload.isOfType(descriptor.getParameterType())) {
                return this.requestPayload;
            }
            answer = this.requestPayload.getProperty(element);
            if (null != answer) {
                return answer;
            }
        }
        throw new IllegalStateException(
                        String.format("requested annotated element %s has not been processed as a parameter, is the API correctly annotated?",
                                        descriptor.toString()));
    }

    /**
     * Set or replaces consumable types. This is because method level annotation
     *
     * @param value
     *            the consumable types.
     * @param isMethod
     *            a flag indicating whether the consumable types are defined at method or type level.
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
     *            a flag indicating whether the consumable types are defined at method or type level.
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

    public Payload getOrCreateRequestPayload(Class<?> type, String name) {
        if (null == this.requestPayload) {
            this.requestPayload = new Payload(type, name);
        }
        return this.requestPayload;
    }

    public Payload getOrCreateResponsePayload(Class<?> type, String name) {
        if (!this.responsePayloads.isEmpty()) {
            for (Payload payload : this.responsePayloads) {
                if (payload.isOfType(type)) {
                    return payload;
                }
            }
        }
        Payload payload = new Payload(type, name);
        this.responsePayloads.add(payload);
        return payload;
    }

    public Document build() {
        return createNewDocument(this.httpMethod).withName(this.name).withDescription(description)
                        .withPath(new Path(buildPath())).withConsumes(this.consumables)
                        .withProduces(this.producible)
                        .withPathParameters(new ArrayList<>(this.pathParameters.values()))
                        .withFormParameters(new ArrayList<>(this.formParameters.values()))
                        .withQueryParameters(new ArrayList<>(this.queryParameters.values()))
                        .withHeaderParameters(new ArrayList<>(this.headerParameters.values()))
                        .withRequestPayload(this.requestPayload)
                        .withResponsePayloads(this.responsePayloads)
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
