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
package codesketch.scriba.test;

import codesketch.scriba.annotations.*;
import codesketch.scriba.annotations.ApiParameter.Type;
import codesketch.scriba.annotations.ApiVerb.Verb;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

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

    @ApiPath("/{isbn}")
    @ApiVerb(Verb.GET)
    @Produces(APPLICATION_JSON)
    @ApiName("Find book")
    @ApiDescription("Find a book among the stored")
    @ApiResponse(type = Book.class)
    public Response find(@NotNull @ApiParameter(value = "isbn", type = Type.PATH) String isbn);

    @POST
    @Produces(APPLICATION_JSON)
    @ApiName("Create a new book")
    @ApiDescription("Create a new book and make it available to the library on-line service")
    @ApiResponse(type = Book.class)
    public Response create(@NotNull @Valid Book book);

    public static class Book {

        @NotNull @JsonProperty private String isbn;
    }
}
