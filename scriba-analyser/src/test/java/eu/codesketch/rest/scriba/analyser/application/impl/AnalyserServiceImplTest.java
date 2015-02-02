package eu.codesketch.rest.scriba.analyser.application.impl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.validation.constraints.NotNull;
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
import org.junit.Test;

import eu.codesketch.rest.scriba.analyser.domain.model.HttpMethods;
import eu.codesketch.rest.scriba.analyser.domain.model.document.Document;
import eu.codesketch.rest.scriba.annotations.ApiDescription;
import eu.codesketch.rest.scriba.annotations.ApiName;

public class AnalyserServiceImplTest {

    private AnalyserServiceImpl testObj = new AnalyserServiceImpl();

    @Test
    public void testAnalise() {
        List<Document> documents = testObj.analyse(BookStoreInterface.class);
        // assert
        assertEquals(5, documents.size());
        for (Document document : documents) {
            if (HttpMethods.GET.getMethod().equals(document.getHttpMethod())) {
                assertTrue(asList("/store/books", "/store/books/{bookId}").contains(
                                document.getPath()));
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
        public void list() {

        }

        @POST
        @Path("/books")
        @ApiName("Store book")
        @ApiDescription("Allows add a new book to the collection")
        @Consumes({ "application/json", "application/xml" })
        @Produces({ "application/json", "application/xml" })
        public void add(Book book) {

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
        public void findById(@PathParam("bookId") Long bookId) {

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
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        public Response addForm(
                        @Size(min = 1, max = 40) @Pattern(regexp = "[a-z]", flags = { Flag.CASE_INSENSITIVE }) @FormParam("name") String name,
                        @QueryParam("collection") Long collection) {
            return null;
        }
    }

    public static class Book {
        @JsonProperty private String author;
        private String name;

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty
        public void setName(String name) {
            this.name = name;
        }
    }
}
