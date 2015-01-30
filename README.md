# scriba
A library for automatically generate machine readable RESTfull APIs documentation.

The library parses the interfaces that define the REST APIs and read the defined annotations in order to understand the APIs structure.

Goal is to have the analysis run at build time so that changes operated during development will be reflected readilly reflected on the documentation.

Currently the library support introspection for JSR-311 and Jackson annotation parsed following the Apache CXF engine logic. A JSON is provided as output that describes the analysed APIs.

## Example
Analysing the following interface:

```java
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
```

will produce:

```json
    [{
        "httpMethod": "POST",
        "path": "/store/books/minimal",
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
        "path": "/store/books/{bookId}",
        "produces": ["application/json"],
        "pathParameters": [{
            "type": "java.lang.Long",
            "name": "bookId"
        }]
    }, {
        "httpMethod": "POST",
        "path": "/store/books",
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
        "path": "/store/books",
        "produces": ["application/json"]
    }, {
        "httpMethod": "DELETE",
        "path": "/store/books/{bookId}",
        "consumes": ["application/json"],
        "produces": ["application/json"],
        "pathParameters": [{
            "type": "java.lang.Long",
            "name": "bookId",
            "defaultValue": "1"
        }]
    }, {
        "httpMethod": "PUT",
        "path": "/store/books",
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
    }]
```
