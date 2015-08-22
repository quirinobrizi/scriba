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
