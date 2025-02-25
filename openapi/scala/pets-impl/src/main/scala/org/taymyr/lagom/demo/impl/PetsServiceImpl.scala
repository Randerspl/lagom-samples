package org.taymyr.lagom.demo.impl

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.ServiceCall
import org.taymyr.lagom.demo.api.{NewPet, Pet, PetsService}

/**
  * Implementation of the PetsService.
  */
class PetsServiceImpl extends PetsService {
  override def find(tags: List[String],
                    limit: Option[Int]): ServiceCall[NotUsed, List[Pet]] = ???

  override def create: ServiceCall[NewPet, Pet] = ???

  override def findBy(id: Long): ServiceCall[NotUsed, Pet] = ???

  override def delete(id: Long): ServiceCall[NotUsed, NotUsed] = ???
}
