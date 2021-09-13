package org.taymyr.lagom.demo.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.Service.{named, pathCall}
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY
import io.swagger.v3.oas.annotations.enums.ParameterStyle
import io.swagger.v3.oas.annotations.info.{Contact, Info, License}
import io.swagger.v3.oas.annotations.media.{ArraySchema, Content, Schema}
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.{OpenAPIDefinition, Operation, Parameter}
import org.taymyr.lagom.scaladsl.openapi.LagomError

/**
 * Simple service with OpenAPI Specification
 */
@OpenAPIDefinition(
    info = new Info(
        version = "1.0.0",
        title = "Swagger Vet database",
        description =
            "A sample secondary lagom API",
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
trait VetsService extends Service {

    @Operation(
        operationId = "find",
        description = "Returns all vets from the system that the user has access to",
        parameters = Array(
            new Parameter(
                name = "tags",
                description = "tags to filter by",
                in = QUERY,
                style = ParameterStyle.FORM,
                array =
                    new ArraySchema( schema = new Schema( implementation = classOf[ String ] ) )
            ),
            new Parameter(
                name = "limit",
                description = "maximum number of results to return",
                in = QUERY,
                schema = new Schema( implementation = classOf[ Int ] )
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
                            schema = new Schema( implementation = classOf[ Vet ] )
                        )
                    )
                )
            ),
            new ApiResponse(
                description = "unexpected error",
                content = Array(
                    new Content(
                        mediaType = "application/json",
                        schema = new Schema( implementation = classOf[ LagomError ] )
                    )
                )
            )
        )
    )
    def find( tags: List[ String ],
              limit: Option[ Int ] ): ServiceCall[ NotUsed, List[ Vet ] ]


    override def descriptor: Descriptor =
        named( "vets" )
            .withCalls(
                pathCall( "/vets?tags&limit", find _ ),
            )
            .withAutoAcl( true )

}
