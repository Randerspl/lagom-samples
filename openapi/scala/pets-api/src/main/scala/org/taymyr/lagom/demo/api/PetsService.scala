package org.taymyr.lagom.demo.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.Service.{named, pathCall, restCall}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import io.swagger.v3.oas.annotations.enums.ParameterIn.{PATH, QUERY}
import io.swagger.v3.oas.annotations.enums.ParameterStyle
import io.swagger.v3.oas.annotations.info.{Contact, Info, License}
import io.swagger.v3.oas.annotations.media.{ArraySchema, Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.{OpenAPIDefinition, Operation, Parameter}
import org.taymyr.lagom.scaladsl.openapi.LagomError

/**
  * Simple service with OpenAPI Specification
  */
@OpenAPIDefinition(
  info = new Info(
    version = "1.0.0",
    title = "Swagger Petstore",
    description =
      "A sample API that uses a petstore as an example to demonstrate features in the OpenAPI 3.0 specification",
    termsOfService = "http://swagger.io/terms/",
    contact = new Contact(
      name = "Swagger API Team",
      email = "apiteam@swagger.io",
      url = "http://swagger.io"
    ),
    license = new License(
      name = "Apache 2.0",
      url = "https://www.apache.org/licenses/LICENSE-2.0.html"
    )
  )
)
trait PetsService extends Service {

  @Operation(
    operationId = "find",
    description = "Returns all pets from the system that the user has access to",
    parameters = Array(
      new Parameter(
        name = "tags",
        description = "tags to filter by",
        in = QUERY,
        style = ParameterStyle.FORM,
        array =
          new ArraySchema(schema = new Schema(implementation = classOf[String]))
      ),
      new Parameter(
        name = "limit",
        description = "maximum number of results to return",
        in = QUERY,
        schema = new Schema(implementation = classOf[Int])
      )
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "pet response",
        content = Array(
          new Content(
            mediaType = "application/json",
            array = new ArraySchema(
              schema = new Schema(implementation = classOf[Pet])
            )
          )
        )
      ),
      new ApiResponse(
        description = "unexpected error",
        content = Array(
          new Content(
            mediaType = "application/json",
            schema = new Schema(implementation = classOf[LagomError])
          )
        )
      )
    )
  )
  def find(tags: List[String],
           limit: Option[Int]): ServiceCall[NotUsed, List[Pet]]

  @Operation(
    operationId = "create",
    description = "Creates a new pet in the store.  Duplicates are allowed",
    requestBody = new RequestBody(
      description = "Pet to add to the store",
      required = true,
      content = Array(
        new Content(
          mediaType = "application/json",
          schema = new Schema(implementation = classOf[NewPet])
        )
      )
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "pet response",
        content = Array(
          new Content(
            mediaType = "application/json",
            schema = new Schema(implementation = classOf[Pet])
          )
        )
      ),
      new ApiResponse(
        description = "unexpected error",
        content = Array(
          new Content(
            mediaType = "application/json",
            schema = new Schema(implementation = classOf[LagomError])
          )
        )
      )
    )
  )
  def create: ServiceCall[NewPet, Pet]

  @Operation(
    operationId = "findById",
    description =
      "Returns a user based on a single ID, if the user does not have access to the pet",
    parameters = Array(
      new Parameter(
        name = "id",
        description = "ID of pet to fetch",
        in = PATH,
        required = true,
        schema = new Schema(implementation = classOf[Long])
      )
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "pet response",
        content = Array(
          new Content(
            mediaType = "application/json",
            schema = new Schema(implementation = classOf[Pet])
          )
        )
      ),
      new ApiResponse(
        description = "unexpected error",
        content = Array(
          new Content(
            mediaType = "application/json",
            schema = new Schema(implementation = classOf[LagomError])
          )
        )
      )
    )
  )
  def findBy(id: Long): ServiceCall[NotUsed, Pet]

  @Operation(
    operationId = "delete",
    description = "deletes a single pet based on the ID supplied",
    parameters = Array(
      new Parameter(
        name = "id",
        description = "ID of pet to delete",
        in = PATH,
        required = true,
        schema = new Schema(implementation = classOf[Long])
      )
    ),
    responses = Array(
      new ApiResponse(responseCode = "204", description = "pet deleted"),
      new ApiResponse(
        description = "unexpected error",
        content = Array(
          new Content(
            mediaType = "application/json",
            schema = new Schema(implementation = classOf[LagomError])
          )
        )
      )
    )
  )
  def delete(id: Long): ServiceCall[NotUsed, NotUsed]

  override def descriptor: Descriptor =
      named("pets")
        .withCalls(
          pathCall("/pets?tags&limit", find _),
          restCall(Method.POST, "/pets", create),
          pathCall("/pets/:id", findBy _),
          restCall(Method.DELETE, "/pets/:id", delete _)
        )
        .withAutoAcl(true)

}
