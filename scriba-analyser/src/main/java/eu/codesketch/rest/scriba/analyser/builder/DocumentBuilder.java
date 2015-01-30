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
package eu.codesketch.rest.scriba.analyser.builder;

import static eu.codesketch.rest.scriba.analyser.helper.StringHelper.join;
import static eu.codesketch.rest.scriba.analyser.model.Document.createNewDocument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import eu.codesketch.rest.scriba.analyser.model.Description;
import eu.codesketch.rest.scriba.analyser.model.Document;
import eu.codesketch.rest.scriba.analyser.model.Name;
import eu.codesketch.rest.scriba.analyser.model.Parameter;
import eu.codesketch.rest.scriba.analyser.model.Path;
import eu.codesketch.rest.scriba.analyser.model.Payload;

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
    private List<Parameter> pathParameters;
    private List<Parameter> formParameters;
    private List<Parameter> queryParameters;
    private List<String> consumables;
    private List<String> producible;
    private Payload payload;
    private Name name;
    private Description description;

    public DocumentBuilder() {
        this.pathSegments = new LinkedList<>();
        this.pathParameters = new ArrayList<>();
        this.formParameters = new ArrayList<>();
        this.queryParameters = new ArrayList<>();
        this.consumables = new ArrayList<>();
        this.producible = new ArrayList<>();
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

    public DocumentBuilder addPathParameter(Parameter pathParameter) {
        this.pathParameters.add(pathParameter);
        return this;
    }

    public DocumentBuilder addFormParameter(Parameter parameter) {
        this.formParameters.add(parameter);
        return this;
    }

    public DocumentBuilder addQueryParameter(Parameter parameter) {
        this.queryParameters.add(parameter);
        return this;
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

    public DocumentBuilder setProducible(List<String> producible) {
        this.producible.addAll(producible);
        return this;
    }

    public DocumentBuilder addPayload(Payload payload) {
        this.payload = payload;
        return this;
    }

    public Document build() {
        return createNewDocument(this.httpMethod).withName(this.name).withDescription(description)
                        .withPath(new Path(buildPath())).withConsumes(this.consumables)
                        .withProduces(this.producible).withPathParameters(this.pathParameters)
                        .withFormParameters(this.formParameters)
                        .withQueryParameters(this.queryParameters).withPayload(this.payload);
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
                        + ", producible=" + producible + ", payload=" + payload + "]";
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
