# scriba
A library for automatically generate machine readable RESTfull APIs documentation.

The library parses the interfaces that define the REST APIs and read the defined annotations in order to understand the APIs structure.

Goal is to have the analysis run at build time so that changes operated during development will be reflected readilly reflected on the documentation.

Currently the library support introspection for JSR-311, JSR-349, Jackson and custom @ApiName and @ApiDescription annotations. A JSON is provided as output that describes the analysed APIs.

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
    @ApiResponse(type = BookMessage.class)
    public Response list() {
        return null;
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
                    @Size(min = 1, max = 40) @Pattern(regexp = "[a-z]", flags = { Flag.CASE_INSENSITIVE }) @FormParam("name") String name,
                    @QueryParam("collection") Long collection) {
        return null;
    }
}
```
```java
public static class Book {
    @JsonProperty private String author;
    private String name;
    @Past @JsonProperty private Date publicationDate;
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

```java
public static class BookMessage {
    @NotNull @JsonProperty private String isbn;
    @JsonProperty private String title;
    @JsonProperty private String author;
    @JsonProperty private Date publicationDate;
          
   public String getIsbn() {
        return isbn;
   }
}
```

will produce:

```json
[{
    "httpMethod": "DELETE",
    "name": "Delete book",
    "path": "/store/books/{bookId}",
    "description": "Allows delete a book from the collection",
    "consumes": ["application/json"],
    "produces": ["application/json"],
    "pathParameters": [{
        "type": "long",
        "name": "bookId",
        "defaultValue": "1",
        "nullable": true,
        "constraints": []
    }],
    "requestPayload": {
        "parameters": []
    },
    "responsePayload": {
        "parameters": []
    }
}, {
    "httpMethod": "GET",
    "name": "List books",
    "path": "/store/books",
    "description": "Retrieves all books",
    "produces": ["application/json"],
    "requestPayload": {
        "parameters": []
    },
    "responsePayload": {
        "parameters": [{
            "type": "string",
            "name": "title",
            "nullable": true,
            "constraints": []
        }, {
            "type": "date",
            "name": "publicationDate",
            "nullable": true,
            "constraints": []
        }, {
            "type": "string",
            "name": "author",
            "nullable": true,
            "constraints": []
        }, {
            "type": "string",
            "name": "isbn",
            "nullable": false,
            "constraints": ["element must not be null"]
        }]
    }
}, {
    "httpMethod": "PUT",
    "name": "Update book",
    "path": "/store/books/{bookId}",
    "description": "Allows update a book already part of the collection",
    "consumes": ["application/json", "application/xml"],
    "produces": ["application/json", "application/xml"],
    "pathParameters": [{
        "type": "long",
        "name": "bookId",
        "nullable": false,
        "constraints": ["element must not be null"]
    }],
    "requestPayload": {
        "parameters": [{
            "type": "string",
            "name": "title",
            "nullable": true,
            "constraints": []
        }, {
            "type": "string",
            "name": "author",
            "nullable": true,
            "constraints": []
        }, {
            "type": "date",
            "name": "publicationDate",
            "nullable": true,
            "constraints": ["date must be in the past considering calendar based on the current timezone and the current locale."]
        }]
    },
    "responsePayload": {
        "parameters": []
    }
}, {
    "httpMethod": "GET",
    "name": "Get book",
    "path": "/store/books/{bookId}",
    "description": "Allows retrieve information about a book",
    "produces": ["application/json"],
    "pathParameters": [{
        "type": "long",
        "name": "bookId",
        "nullable": true,
        "constraints": []
    }],
    "requestPayload": {
        "parameters": []
    },
    "responsePayload": {
        "parameters": [{
            "type": "string",
            "name": "title",
            "nullable": true,
            "constraints": []
        }, {
            "type": "date",
            "name": "publicationDate",
            "nullable": true,
            "constraints": []
        }, {
            "type": "string",
            "name": "author",
            "nullable": true,
            "constraints": []
        }, {
            "type": "string",
            "name": "isbn",
            "nullable": false,
            "constraints": ["element must not be null"]
        }]
    }
}, {
    "httpMethod": "POST",
    "name": "Create minimal book",
    "path": "/store/books/minimal",
    "description": "Allows create a new book with only its name",
    "consumes": ["application/x-www-form-urlencoded"],
    "produces": ["application/json"],
    "formParameters": [{
        "type": "string",
        "name": "name",
        "nullable": true,
        "constraints": ["element size must be higher or equal to 1 and lower or equal to 40", "value must match the specified regular expression [a-z] with flags CASE_INSENSITIVE"]
    }],
    "queryParameters": [{
        "type": "long",
        "name": "collection",
        "nullable": true,
        "constraints": []
    }],
    "requestPayload": {
        "parameters": []
    },
    "responsePayload": {
        "parameters": [{
            "type": "string",
            "name": "title",
            "nullable": true,
            "constraints": []
        }, {
            "type": "date",
            "name": "publicationDate",
            "nullable": true,
            "constraints": []
        }, {
            "type": "string",
            "name": "author",
            "nullable": true,
            "constraints": []
        }, {
            "type": "string",
            "name": "isbn",
            "nullable": false,
            "constraints": ["element must not be null"]
        }]
    }
}, {
    "httpMethod": "POST",
    "name": "Store book",
    "path": "/store/books",
    "description": "Allows add a new book to the collection",
    "consumes": ["application/json", "application/xml"],
    "produces": ["application/json", "application/xml"],
    "requestPayload": {
        "parameters": [{
            "type": "string",
            "name": "title",
            "nullable": true,
            "constraints": []
        }, {
            "type": "string",
            "name": "author",
            "nullable": true,
            "constraints": []
        }, {
            "type": "date",
            "name": "publicationDate",
            "nullable": true,
            "constraints": ["date must be in the past considering calendar based on the current timezone and the current locale."]
        }]
    },
    "responsePayload": {
        "parameters": []
    }
}]
```
