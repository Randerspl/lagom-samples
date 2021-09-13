package org.taymyr.lagom.demo.api

import io.swagger.v3.oas.annotations.media.Schema

import scala.annotation.meta.field
import scala.beans.BeanProperty

case class Vet( @( Schema@field )( required = true )
                @BeanProperty id: Long,
                @( Schema@field )( required = true )
                @BeanProperty name: String,
                @BeanProperty tag: String )

object Vet {

    import play.api.libs.json._

    implicit val format: Format[ Vet ] = Json.format[ Vet ]
}

