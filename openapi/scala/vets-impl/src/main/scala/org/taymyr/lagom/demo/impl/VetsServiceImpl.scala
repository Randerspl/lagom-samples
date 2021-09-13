package org.taymyr.lagom.demo.impl

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.ServiceCall
import org.taymyr.lagom.demo.api.{PetsService, Vet, VetsService}

/**
 * Implementation of the PetsService.
 */
class VetsServiceImpl( petsService: PetsService ) extends VetsService {
    override def find( tags: List[ String ], limit: Option[ Int ] ): ServiceCall[ NotUsed, List[ Vet ] ] = ???
}
