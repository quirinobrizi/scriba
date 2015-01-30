package eu.codesketch.rest.scriba.analyser.impl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

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

import eu.codesketch.rest.scriba.analyser.model.Document;
import eu.codesketch.rest.scriba.analyser.model.HttpMethods;

public class Jsr311AnalyserTest {

    private Jsr311Analyser testObj = new Jsr311Analyser();

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
        public void list() {

        }

        @POST
        @Path("/books")
        @Consumes({ "application/json", "application/xml" })
        @Produces({ "application/json", "application/xml" })
        public void add(Book book) {

        }

        @PUT
        @Path("/books")
        @Consumes({ "application/json", "application/xml" })
        @Produces({ "application/json", "application/xml" })
        public void update(Book book) {

        }

        @GET
        @Path("/books/{bookId}")
        public void findById(@PathParam("bookId") Long bookId) {

        }

        @DELETE
        @Path("/books/{bookId}")
        public void delete(@DefaultValue("1") @PathParam("bookId") Long bookId) {

        }

        @POST
        @Path("/books/minimal")
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        public Response addForm(@FormParam("name") String name,
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
