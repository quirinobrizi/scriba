package codesketch.scriba.analyser.application.impl;

import codesketch.scriba.analyser.domain.model.HttpMethods;
import codesketch.scriba.analyser.domain.model.document.Document;
import codesketch.scriba.analyser.infrastructure.guice.ScribaInjector;
import codesketch.scriba.annotations.ApiDescription;
import codesketch.scriba.annotations.ApiName;
import codesketch.scriba.annotations.ApiResponse;
import com.google.inject.Guice;
import org.codehaus.jackson.annotate.JsonProperty;
import org.junit.Before;
import org.junit.Test;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        @ApiResponse(type = BookMessage.class)
        public Response list() {
            return null;
        }

        @POST
        @Valid
        @Path("/books")
        @ApiName("Store book")
        @ApiDescription("Allows add a new book to the collection")
        @Consumes({"application/json", "application/xml"})
        @Produces({"application/json", "application/xml"})
        public void add(@Valid Book book) {

        }

        @PUT
        @Path("/books/{bookId}")
        @ApiName("Update book")
        @ApiDescription("Allows update a book already part of the collection")
        @Consumes({"application/json", "application/xml"})
        @Produces({"application/json", "application/xml"})
        public void update(@NotNull @PathParam("bookId") Long bookId, Book book) {

        }

        @GET
        @Path("/books/{bookId}")
        @ApiName("Get book")
        @ApiDescription("Allows retrieve information about a book")
        @ApiResponse(type = BookMessage.class)
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
        @ApiResponse(type = BookMessage.class)
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        public Response addForm(
                @Size(min = 1, max = 40, message = "Book name should be of size between {min} and {max}") @Pattern(regexp = "[a-z]", flags = {Flag.CASE_INSENSITIVE}) @FormParam("name") String name,
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
        @NotNull @JsonProperty private String isbn;
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
}
