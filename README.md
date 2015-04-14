# scriba
A library for automatically generate machine readable RESTfull APIs documentation.

The library parses the interfaces that define the REST APIs and read the defined annotations in order to understand the APIs structure.

Goal is to have the analysis run at build time so that changes operated during development will be reflected readilly reflected on the documentation.

Currently the library support introspection for JSR-311, JSR-349, Jackson and following custom annotations:
 * @ApiName, define the API name
 * @ApiDescription, provide the API behaviour description
 * @ApiConsumes, define the media type accepted as input by the API
 * @ApiDefault, define the default value for a field, parameter or method return 
 * @ApiParameter, define the name and type of an API parameters, type describe from where the parameter is retrieved, i.e query string, form body, etc.
 * @ApiPath, define the path this API will respond to requests
 * @ApiProduces, define the media type provided as output by the API
 * @ApiResponse, define the response of the API
 * @ApiVerb, define the verb the API respond.
 
 A JSON is provided as output that describes the analysed APIs.

The custom annotation are not needed and can be avoided with the pitfall that the API name and description will not be present on the response. 

## Example
Analysing the following interface:

```java
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

    public static class Book {

        @NotNull @JsonProperty private String isbn;
    }
}
```

will produce:

```json
[{
    "httpMethod": "GET",
    "name": "List books",
    "path": "books",
    "description": "List all present books",
    "produces": ["application/json"],
    "requestPayload": {
        "properties": []
    },
    "responsePayload": {
        "properties": [{
            "type": "string",
            "name": "isbn",
            "nullable": false,
            "constraints": ["element must not be null"]
        }]
    },
    "messages": [{
        "status": 400,
        "message": "may not be null"
    }]
}, {
    "httpMethod": "GET",
    "name": "Find book",
    "path": "books/{isbn}",
    "description": "Find a book among the stored",
    "produces": ["application/json"],
    "pathParameters": [{
        "type": "string",
        "name": "isbn",
        "nullable": false,
        "constraints": ["element must not be null"]
    }],
    "requestPayload": {
        "properties": []
    },
    "responsePayload": {
        "properties": [{
            "type": "string",
            "name": "isbn",
            "nullable": false,
            "constraints": ["element must not be null"]
        }]
    },
    "messages": [{
        "status": 400,
        "message": "may not be null"
    }, {
        "status": 400,
        "message": "may not be null"
    }]
}]
```
