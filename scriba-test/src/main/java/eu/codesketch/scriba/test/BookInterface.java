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
package eu.codesketch.scriba.test;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.annotate.JsonProperty;

import eu.codesketch.scriba.rest.annotations.ApiDescription;
import eu.codesketch.scriba.rest.annotations.ApiName;
import eu.codesketch.scriba.rest.annotations.ApiResponse;

/**
 * 
 *
 * @author quirino.brizi
 * @since 3 Feb 2015
 *
 */
@Path("books")
public interface BookInterface {

    @GET
    @Produces(APPLICATION_JSON)
    @ApiName("List books")
    @ApiDescription("List all present books")
    @ApiResponse(type = Book.class)
    public Response list();

    public static class Book {

        @NotNull @JsonProperty private String isbn;
    }
}
