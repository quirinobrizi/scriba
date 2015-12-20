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

import javax.ws.rs.HttpMethod;

/**
 * Representation of an HTTP Method.
 *
 * @author quirino.brizi
 * @since 29 Jan 2015
 *
 */
public enum HttpMethods {

    // @formatter:off
        GET(HttpMethod.GET, false), 
        POST(HttpMethod.POST, true), 
        PUT(HttpMethod.PUT, true), 
        DELETE(HttpMethod.DELETE, true),
        HEAD(HttpMethod.HEAD, true),
        OPTIONS(HttpMethod.OPTIONS, true);
    // @formatter:on

    private String method;
    private Boolean hasPayload;

    private HttpMethods(String method, boolean hasPayload) {
        this.method = method;
        this.hasPayload = hasPayload;
    }

    public static HttpMethods lookupHttpMethod(String httpMethod) {
        for (HttpMethods value : values()) {
            if (value.method.equalsIgnoreCase(httpMethod)) {
                return value;
            }
        }
        throw new IllegalArgumentException(String.format("unknown HTTP Method %s", httpMethod));
    }

    public String getMethod() {
        return method;
    }

    public Boolean hasPayload() {
        return hasPayload;
    }

}
