# scriba
A library for automatically generate machine readable RESTfull APIs documentation.

The library parses the interfaces that define the REST APIs and read the defined annotations in order to understand the APIs structure.

Goal is to have the analysis run at build time so that changes operated during development will be reflected readilly reflected on the documentation.

Currently the library support introspection for JSR-311, Jackson and custom @ApiName and @ApiDescription annotations. A JSON is provided as output that describes the analysed APIs.

The custom annotation are not needed and can be avoided with the pitfall that the API name and description will not be present on the response. 

## Example
Analysing the following interface:

```java
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
    public void update(@PathParam("bookId") Long bookId, Book book) {
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
    public Response addForm(@FormParam("name") String name,
                    @QueryParam("collection") Long collection) {
        return null;
    }
}
```
```java
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
```

will produce:

```json
    [{
    "httpMethod": "POST",
    "name": "Create minimal book",
    "path": "/store/books/minimal",
    "description": "Allows create a new book with only its name",
    "consumes": ["application/x-www-form-urlencoded"],
    "produces": ["application/json"],
    "formParameters": [{
        "type": "java.lang.String",
        "name": "name"
    }],
    "queryParameters": [{
        "type": "java.lang.Long",
        "name": "collection"
    }]
}, {
    "httpMethod": "GET",
    "name": "Get book",
    "path": "/store/books/{bookId}",
    "description": "Allows retrieve information about a book",
    "produces": ["application/json"],
    "pathParameters": [{
        "type": "java.lang.Long",
        "name": "bookId"
    }]
}, {
    "httpMethod": "POST",
    "name": "Store book",
    "path": "/store/books",
    "description": "Allows add a new book to the collection",
    "consumes": ["application/json", "application/xml"],
    "produces": ["application/json", "application/xml"],
    "payload": {
        "parameters": [{
            "type": "java.lang.String",
            "name": "name"
        }, {
            "type": "java.lang.String",
            "name": "author"
        }]
    }
}, {
    "httpMethod": "GET",
    "name": "List books",
    "path": "/store/books",
    "description": "Retrieves all books",
    "produces": ["application/json"]
}, {
    "httpMethod": "DELETE",
    "name": "Delete book",
    "path": "/store/books/{bookId}",
    "description": "Allows delete a book from the collection",
    "consumes": ["application/json"],
    "produces": ["application/json"],
    "pathParameters": [{
        "type": "java.lang.Long",
        "name": "bookId",
        "defaultValue": "1"
    }]
}, {
    "httpMethod": "PUT",
    "name": "Update book",
    "path": "/store/books/{bookId}",
    "description": "Allows update a book already part of the collection",
    "consumes": ["application/json", "application/xml"],
    "produces": ["application/json", "application/xml"],
    "pathParameters": [{
        "type": "java.lang.Long",
        "name": "bookId"
    }],
    "payload": {
        "parameters": [{
            "type": "java.lang.String",
            "name": "name"
        }, {
            "type": "java.lang.String",
            "name": "author"
        }]
    }
}]
```
