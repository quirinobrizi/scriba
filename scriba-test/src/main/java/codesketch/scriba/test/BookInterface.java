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
package codesketch.scriba.test;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.annotate.JsonProperty;

import codesketch.scriba.annotations.ApiDescription;
import codesketch.scriba.annotations.ApiName;
import codesketch.scriba.annotations.ApiParameter;
import codesketch.scriba.annotations.ApiParameter.Type;
import codesketch.scriba.annotations.ApiPath;
import codesketch.scriba.annotations.ApiResponse;
import codesketch.scriba.annotations.ApiResponses;
import codesketch.scriba.annotations.ApiVerb;
import codesketch.scriba.annotations.ApiVerb.Verb;

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
    @ApiResponses({ @ApiResponse(type = Book.class, responseCode = 201),
                    @ApiResponse(type = Error.class, responseCode = 500, message = "internal server error", success = false) })
    public Response create(@NotNull(message = "book  parameter must be provided") @Valid Book book);

    public static class Book {

        @NotNull @JsonProperty private String isbn;
    }

    public static class Error {
        @NotNull @JsonProperty private String message;
    }
}
