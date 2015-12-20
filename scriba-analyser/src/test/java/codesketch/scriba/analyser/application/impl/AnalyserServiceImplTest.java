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

package codesketch.scriba.analyser.application.impl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.annotate.JsonProperty;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;

import codesketch.scriba.analyser.domain.model.HttpMethods;
import codesketch.scriba.analyser.domain.model.document.Document;
import codesketch.scriba.analyser.infrastructure.guice.ScribaInjector;
import codesketch.scriba.annotations.ApiDescription;
import codesketch.scriba.annotations.ApiName;
import codesketch.scriba.annotations.ApiResponse;
import codesketch.scriba.annotations.ApiResponses;

public class AnalyserServiceImplTest {

    private AnalyserServiceImpl testObj;

    @Before
    public void setUp() {
        testObj = Guice.createInjector(new ScribaInjector()).getInstance(AnalyserServiceImpl.class);
    }

    @Test
    public void testAnalise() {
        List<Document> documents = testObj.analyse(BookStoreInterface.class);
        // assert
        assertEquals(6, documents.size());
        for (Document document : documents) {
            if (HttpMethods.GET.getMethod().equals(document.getHttpMethod())) {
                assertTrue(asList("/store/books", "/store/books/{bookId}")
                                .contains(document.getPath()));
            }
        }
    }

    @Path("/store")
    @Consumes("application/json")
    @Produces("application/json")
    public static class BookStoreInterface {

        @GET
        @Path("/books")
        @ApiName("List books")
        @ApiDescription("Retrieves all books")
        @ApiResponse(type = BookMessage.class)
        public Response list() {
            return null;
        }

        @POST
        @Valid
        @Path("/books")
        @ApiName("Store book")
        @ApiDescription("Allows add a new book to the collection")
        @Consumes({ "application/json", "application/xml" })
        @Produces({ "application/json", "application/xml" })
        public void add(@Valid Book book) {

        }

        @PUT
        @Path("/books/{bookId}")
        @ApiName("Update book")
        @ApiDescription("Allows update a book already part of the collection")
        @Consumes({ "application/json", "application/xml" })
        @Produces({ "application/json", "application/xml" })
        public void update(@NotNull @PathParam("bookId") Long bookId, Book book) {

        }

        @GET
        @Path("/books/{bookId}")
        @ApiName("Get book")
        @ApiDescription("Allows retrieve information about a book")
        @ApiResponses({ @ApiResponse(type = BookMessage.class) })
        public Response findById(@PathParam("bookId") Long bookId) {
            return null;
        }

        @DELETE
        @Path("/books/{bookId}")
        @ApiName("Delete book")
        @ApiDescription("Allows delete a book from the collection")
        public void delete(@DefaultValue("1") @PathParam("bookId") Long bookId) {

        }

        @POST
        @Path("/books/minimal")
        @ApiName("Create minimal book")
        @ApiDescription("Allows create a new book with only its name")
        @ApiResponses({ @ApiResponse(type = BookMessage.class),
                        @ApiResponse(type = ErrorMessage.class, success = false, responseCode = 401, message = "Unauthorized"),
                        @ApiResponse(type = ErrorMessage.class, success = false, responseCode = 500, message = "Internal server error") })
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        public Response addForm(
                        @Size(min = 1, max = 40, message = "Book name should be of size between {min} and {max}") @Pattern(regexp = "[a-z]", flags = {
                                        Flag.CASE_INSENSITIVE }) @FormParam("name") String name,
                        @QueryParam("collection") Long collection) {
            return null;
        }
    }

    public static class Book {
        @JsonProperty private String author;
        private Title title;
        @Past @JsonProperty private Date publicationDate;

        @JsonProperty("title")
        public Title getTitle() {
            return title;
        }

        @JsonProperty
        public void setTitle(Title title) {
            this.title = title;
        }
    }

    public static class BookMessage {
        @NotNull(message = "ISBN parameter is not nullable and must be provided") @JsonProperty private String isbn;
        @JsonProperty private Title title;
        @JsonProperty private String author;
        @JsonProperty private Date publicationDate;

        public String getIsbn() {
            return isbn;
        }
    }

    public static class Title {
        @JsonProperty String title;
    }

    public static class ErrorMessage {
        @JsonProperty Integer code;
        @JsonProperty String message;
    }
}
