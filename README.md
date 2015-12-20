# Scriba
Scriba is a code analyzer able to generate a JSON representation of your APIs. It integrates with Maven build system via a plugin so that it can integrate in a less invasive way into your build process.
Scriba can be configured to send the generated JSON representation automatically to a remote service via HTTP(S) protocol or to write it to a file so that you can post-process your API documentation as per your needs.

Scriba parses the interfaces that define your REST APIs inspecting the defined annotations in order to understand the APIs structure.

Goal is to have the analysis run at build time so that changes operated during development will be immediately and automatically reflected on the API documentation.

Currently the library support introspection for JSR-311, JSR-349, Jackson and following custom annotations:
 * @ApiName, allows to define the API name
 * @ApiDescription, allows to provide a description of the API behaviour
 * @ApiConsumes, allows to define the media type accepted as input by the API
 * @ApiDefault, allows to define the default value for a field, parameter or method return 
 * @ApiParameter, allows to define the name and type of an API parameters, type describe from where the parameter is retrieved, i.e query string, form body, etc.
 * @ApiPath, allows to define the path this API will respond to requests
 * @ApiProduces, allows to define the media type provided as output by the API
 * @ApiResponse, allows to define the API response type, its numerical response code and a message for documentation purposes
 * @ApiResponses, allows to define all responses the an API can return. Accepts an array of @ApiResponse
 * @ApiVerb, allows to define the verb the API respond.
 
 A JSON describing the analysed APIs is provided as output.

Scriba annotations are useful on all the cases where no other supported annotations are present - i.e. [Apache Camel Rest](http://camel.apache.org/rest.html) - in this case your service can be annotated and given to Scriba analyser in order to generate the final document.
As per the above Scriba annotation are not strictly required and can be avoided. Pitfall is that the API name and description will not be present on the response.

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

    @POST
    @Produces(APPLICATION_JSON)
    @ApiName("Create a new book")
    @ApiDescription("Create a new book and make it available to the library on-line service")
    @ApiResponse(type = Book.class, responseCode = 201)
    public Response create(@NotNull(message = "book  parameter must be provided") @Valid Book book);

    public static class Book {

        @NotNull @JsonProperty private String isbn;
    }
}
```

will produce:
* with Scriba analyser

```json
[
    {
        "httpMethod": "GET",
        "name": "List books",
        "path": "books",
        "description": "List all present books",
        "produces": [
            "application/json"
        ],
        "responsePayload": {
            "nullable": false,
            "type": "codesketch.scriba.test.BookInterface$Book",
            "properties": [
                {
                    "name": "isbn",
                    "nullable": false,
                    "constraints": [
                        "element must not be null"
                    ],
                    "type": "string"
                }
            ]
        },
        "messages": [
            {
                "status": 200,
                "message": "",
                "success": true
            },
            {
                "status": 400,
                "message": "may not be null",
                "success": false
            }
        ]
    },
    {
        "httpMethod": "GET",
        "name": "Find book",
        "path": "books/{isbn}",
        "description": "Find a book among the stored",
        "produces": [
            "application/json"
        ],
        "pathParameters": [
            {
                "name": "isbn",
                "nullable": false,
                "constraints": [
                    "element must not be null"
                ],
                "type": "string"
            }
        ],
        "responsePayload": {
            "nullable": false,
            "type": "codesketch.scriba.test.BookInterface$Book",
            "properties": [
                {
                    "name": "isbn",
                    "nullable": false,
                    "constraints": [
                        "element must not be null"
                    ],
                    "type": "string"
                }
            ]
        },
        "messages": [
            {
                "status": 200,
                "message": "",
                "success": true
            },
            {
                "status": 400,
                "message": "may not be null",
                "success": false
            }
        ]
    },
    {
        "httpMethod": "POST",
        "name": "Create a new book",
        "path": "books",
        "description": "Create a new book and make it available to the library on-line service",
        "produces": [
            "application/json"
        ],
        "requestPayload": {
            "nullable": false,
            "constraints": [
                "element must not be null"
            ],
            "type": "codesketch.scriba.test.BookInterface$Book",
            "properties": [
                {
                    "name": "isbn",
                    "nullable": false,
                    "constraints": [
                        "element must not be null"
                    ],
                    "type": "string"
                }
            ]
        },
        "responsePayload": {
            "nullable": false,
            "type": "codesketch.scriba.test.BookInterface$Book",
            "properties": [
                {
                    "name": "isbn",
                    "nullable": false,
                    "constraints": [
                        "element must not be null"
                    ],
                    "type": "string"
                }
            ]
        },
        "messages": [
            {
                "status": 201,
                "message": "",
                "success": true
            },
            {
                "status": 400,
                "message": "may not be null",
                "success": false
            },
            {
                "status": 400,
                "message": "book  parameter must be provided",
                "success": false
            }
        ]
    }
]
```

* with Scriba mavne plugin

```json
{
    "version": "0.0.1-SNAPSHOT",
    "content": "{\"version\":\"0.0.1-SNAPSHOT\",\"environments\":[{\"name\":\"test\",\"endpoint\":\"http://test.endpoint.org\"}],\"documents\":[{\"httpMethod\":\"GET\",\"name\":\"List books\",\"path\":\"books\",\"description\":\"List all present books\",\"produces\":[\"application/json\"],\"responsePayload\":{\"nullable\":false,\"type\":\"codesketch.scriba.test.BookInterface$Book\",\"properties\":[{\"name\":\"isbn\",\"nullable\":false,\"constraints\":[\"element must not be null\"],\"type\":\"string\"}]},\"messages\":[{\"status\":200,\"message\":\"\",\"success\":true},{\"status\":400,\"message\":\"may not be null\",\"success\":false}]},{\"httpMethod\":\"GET\",\"name\":\"Find book\",\"path\":\"books/{isbn}\",\"description\":\"Find a book among the stored\",\"produces\":[\"application/json\"],\"pathParameters\":[{\"name\":\"isbn\",\"nullable\":false,\"constraints\":[\"element must not be null\"],\"type\":\"string\"}],\"responsePayload\":{\"nullable\":false,\"type\":\"codesketch.scriba.test.BookInterface$Book\",\"properties\":[{\"name\":\"isbn\",\"nullable\":false,\"constraints\":[\"element must not be null\"],\"type\":\"string\"}]},\"messages\":[{\"status\":200,\"message\":\"\",\"success\":true},{\"status\":400,\"message\":\"may not be null\",\"success\":false}]},{\"httpMethod\":\"POST\",\"name\":\"Create a new book\",\"path\":\"books\",\"description\":\"Create a new book and make it available to the library on-line service\",\"produces\":[\"application/json\"],\"requestPayload\":{\"nullable\":false,\"constraints\":[\"element must not be null\"],\"type\":\"codesketch.scriba.test.BookInterface$Book\",\"properties\":[{\"name\":\"isbn\",\"nullable\":false,\"constraints\":[\"element must not be null\"],\"type\":\"string\"}]},\"responsePayload\":{\"nullable\":false,\"type\":\"codesketch.scriba.test.BookInterface$Book\",\"properties\":[{\"name\":\"isbn\",\"nullable\":false,\"constraints\":[\"element must not be null\"],\"type\":\"string\"}]},\"messages\":[{\"status\":201,\"message\":\"\",\"success\":true},{\"status\":400,\"message\":\"may not be null\",\"success\":false},{\"status\":400,\"message\":\"book  parameter must be provided\",\"success\":false}]}],\"updateTimestamp\":1447321264963,\"updater\":\"quirino\"}"
}
```

The version produced by Scriba maven plugin is reach in terms of information and ready to be sent as a JSON payload to a receiving API.

## Integration

### Integrate Scriba annotations
Scriba annotations can be integrated into your application adding following dependency to your Apache Maven pom.xml:

```xml
<dependency>
 	<groupId>codesketch.scriba</groupId>
  	<artifactId>scriba-annotations</artifactId>
  	<version>[version]</version>
</dependency>
```