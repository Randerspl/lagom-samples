package org.taymyr.lagom.demo.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import org.taymyr.lagom.demo.api.{PetsService, VetsService}
import org.taymyr.lagom.scaladsl.openapi.OpenAPIRouter
import play.api.libs.ws.ahc.AhcWSComponents

class VetsLoader extends LagomApplicationLoader {

    override def load( context: LagomApplicationContext ): LagomApplication =
        new VetsApplication( context ) {
            override def serviceLocator: ServiceLocator = NoServiceLocator
        }

    override def loadDevMode( context: LagomApplicationContext ): LagomApplication =
        new VetsApplication( context ) with LagomDevModeComponents

    override def describeService = Some( readDescriptor[ PetsService ] )
}

abstract class VetsApplication( context: LagomApplicationContext )
    extends LagomApplication( context )
        with AhcWSComponents {

    lazy val petsService: PetsService = serviceClient.implement[ PetsService ]

    // Bind the service that this server provides
    override lazy val lagomServer = {
        val service = wire[ VetsServiceImpl ]
        serverFor[ VetsService ]( service )
            .additionalRouter( wire[ OpenAPIRouter ].router( service ) )
    }

}
