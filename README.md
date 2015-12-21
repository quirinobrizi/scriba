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

```

will produce:
* with Scriba analyser

```json
{
    "version":"1.0.0",
    "environments":[
        {
            "name":"test",
            "endpoint":"http://test.endpoint.org"
        }
    ],
    "documents":[
        {
            "httpMethod":"POST",
            "name":"Store book",
            "path":"/store/books",
            "description":"Allows add a new book to the collection",
            "consumes":[
                "application/json",
                "application/xml"
            ],
            "produces":[
                "application/json",
                "application/xml"
            ],
            "parameters":{
                "form":[
                ],
                "query":[
                ],
                "path":[
                ],
                "header":[
                ]
            },
            "payloads":{
                "response":[
                ],
                "request":[
                    {
                        "nullable":false,
                        "type":"codesketch.scriba.analyser.application.impl.AnalyserServiceImplTest$Book",
                        "properties":[
                            {
                                "name":"author",
                                "nullable":true,
                                "type":"string"
                            },
                            {
                                "name":"title",
                                "nullable":true,
                                "properties":[
                                    {
                                        "name":"title",
                                        "nullable":true,
                                        "type":"string"
                                    }
                                ]
                            },
                            {
                                "name":"publicationDate",
                                "nullable":true,
                                "constraints":[
                                    "date must be in the past considering calendar based on the current timezone and the current locale."
                                ],
                                "type":"date"
                            }
                        ]
                    }
                ]
            },
            "messages":[
                {
                    "status":400,
                    "message":"must be in the past",
                    "success":false
                }
            ]
        },
        {
            "httpMethod":"POST",
            "name":"Create minimal book",
            "path":"/store/books/minimal",
            "description":"Allows create a new book with only its name",
            "consumes":[
                "application/x-www-form-urlencoded"
            ],
            "produces":[
                "application/json"
            ],
            "parameters":{
                "form":[
                    {
                        "name":"name",
                        "nullable":true,
                        "constraints":[
                            "value must match the specified regular expression [a-z] with flags CASE_INSENSITIVE",
                            "element size must be higher or equal to 1 and lower or equal to 40"
                        ],
                        "type":"string"
                    }
                ],
                "query":[
                    {
                        "name":"collection",
                        "nullable":true,
                        "type":"long"
                    }
                ],
                "path":[
                ],
                "header":[
                ]
            },
            "payloads":{
                "response":[
                    {
                        "nullable":false,
                        "type":"codesketch.scriba.analyser.application.impl.AnalyserServiceImplTest$BookMessage",
                        "properties":[
                            {
                                "name":"publicationDate",
                                "nullable":true,
                                "type":"date"
                            },
                            {
                                "name":"isbn",
                                "nullable":false,
                                "constraints":[
                                    "element must not be null"
                                ],
                                "type":"string"
                            },
                            {
                                "name":"author",
                                "nullable":true,
                                "type":"string"
                            },
                            {
                                "name":"title",
                                "nullable":true,
                                "properties":[
                                    {
                                        "name":"title",
                                        "nullable":true,
                                        "type":"string"
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        "nullable":false,
                        "type":"codesketch.scriba.analyser.application.impl.AnalyserServiceImplTest$ErrorMessage",
                        "properties":[
                            {
                                "name":"code",
                                "nullable":true,
                                "type":"integer"
                            },
                            {
                                "name":"message",
                                "nullable":true,
                                "type":"string"
                            }
                        ]
                    }
                ],
                "request":[
                ]
            },
            "messages":[
                {
                    "status":200,
                    "message":"",
                    "success":true
                },
                {
                    "status":400,
                    "message":"ISBN parameter is not nullable and must be provided",
                    "success":false
                },
                {
                    "status":400,
                    "message":"Book name should be of size between 1 and 40",
                    "success":false
                },
                {
                    "status":401,
                    "message":"Unauthorized",
                    "success":false
                },
                {
                    "status":400,
                    "message":"must match \\"                    [
                        a-z
                    ]                    \\"",
                    "success":false
                },
                {
                    "status":500,
                    "message":"Internal server error",
                    "success":false
                }
            ]
        },
        {
            "httpMethod":"GET",
            "name":"List books",
            "path":"/store/books",
            "description":"Retrieves all books",
            "produces":[
                "application/json"
            ],
            "parameters":{
                "form":[
                ],
                "query":[
                ],
                "path":[
                ],
                "header":[
                ]
            },
            "payloads":{
                "response":[
                    {
                        "nullable":false,
                        "type":"codesketch.scriba.analyser.application.impl.AnalyserServiceImplTest$BookMessage",
                        "properties":[
                            {
                                "name":"publicationDate",
                                "nullable":true,
                                "type":"date"
                            },
                            {
                                "name":"isbn",
                                "nullable":false,
                                "constraints":[
                                    "element must not be null"
                                ],
                                "type":"string"
                            },
                            {
                                "name":"author",
                                "nullable":true,
                                "type":"string"
                            },
                            {
                                "name":"title",
                                "nullable":true,
                                "properties":[
                                    {
                                        "name":"title",
                                        "nullable":true,
                                        "type":"string"
                                    }
                                ]
                            }
                        ]
                    }
                ],
                "request":[
                ]
            },
            "messages":[
                {
                    "status":200,
                    "message":"",
                    "success":true
                },
                {
                    "status":400,
                    "message":"ISBN parameter is not nullable and must be provided",
                    "success":false
                }
            ]
        },
        {
            "httpMethod":"DELETE",
            "name":"Delete book",
            "path":"/store/books/{bookId}",
            "description":"Allows delete a book from the collection",
            "consumes":[
                "application/json"
            ],
            "produces":[
                "application/json"
            ],
            "parameters":{
                "form":[
                ],
                "query":[
                ],
                "path":[
                    {
                        "name":"bookId",
                        "defaultValue":"1",
                        "nullable":true,
                        "type":"long"
                    }
                ],
                "header":[
                ]
            },
            "payloads":{
                "response":[
                ],
                "request":[
                ]
            }
        },
        {
            "httpMethod":"GET",
            "name":"Get book",
            "path":"/store/books/{bookId}",
            "description":"Allows retrieve information about a book",
            "produces":[
                "application/json"
            ],
            "parameters":{
                "form":[
                ],
                "query":[
                ],
                "path":[
                    {
                        "name":"bookId",
                        "nullable":true,
                        "type":"long"
                    }
                ],
                "header":[
                ]
            },
            "payloads":{
                "response":[
                    {
                        "nullable":false,
                        "type":"codesketch.scriba.analyser.application.impl.AnalyserServiceImplTest$BookMessage",
                        "properties":[
                            {
                                "name":"publicationDate",
                                "nullable":true,
                                "type":"date"
                            },
                            {
                                "name":"isbn",
                                "nullable":false,
                                "constraints":[
                                    "element must not be null"
                                ],
                                "type":"string"
                            },
                            {
                                "name":"author",
                                "nullable":true,
                                "type":"string"
                            },
                            {
                                "name":"title",
                                "nullable":true,
                                "properties":[
                                    {
                                        "name":"title",
                                        "nullable":true,
                                        "type":"string"
                                    }
                                ]
                            }
                        ]
                    }
                ],
                "request":[
                ]
            },
            "messages":[
                {
                    "status":200,
                    "message":"",
                    "success":true
                },
                {
                    "status":400,
                    "message":"ISBN parameter is not nullable and must be provided",
                    "success":false
                }
            ]
        },
        {
            "httpMethod":"PUT",
            "name":"Update book",
            "path":"/store/books/{bookId}",
            "description":"Allows update a book already part of the collection",
            "consumes":[
                "application/json",
                "application/xml"
            ],
            "produces":[
                "application/json",
                "application/xml"
            ],
            "parameters":{
                "form":[
                ],
                "query":[
                ],
                "path":[
                    {
                        "name":"bookId",
                        "nullable":false,
                        "constraints":[
                            "element must not be null"
                        ],
                        "type":"long"
                    }
                ],
                "header":[
                ]
            },
            "payloads":{
                "response":[
                ],
                "request":[
                    {
                        "nullable":false,
                        "type":"codesketch.scriba.analyser.application.impl.AnalyserServiceImplTest$Book",
                        "properties":[
                            {
                                "name":"author",
                                "nullable":true,
                                "type":"string"
                            },
                            {
                                "name":"title",
                                "nullable":true,
                                "properties":[
                                    {
                                        "name":"title",
                                        "nullable":true,
                                        "type":"string"
                                    }
                                ]
                            },
                            {
                                "name":"publicationDate",
                                "nullable":true,
                                "constraints":[
                                    "date must be in the past considering calendar based on the current timezone and the current locale."
                                ],
                                "type":"date"
                            }
                        ]
                    }
                ]
            },
            "messages":[
                {
                    "status":400,
                    "message":"may not be null",
                    "success":false
                },
                {
                    "status":400,
                    "message":"must be in the past",
                    "success":false
                }
            ]
        }
    ],
    "updateTimestamp":1450704313770,
    "updater":"quirino"
}
```

* with Scriba maven plugin

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