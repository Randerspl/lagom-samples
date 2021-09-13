ThisBuild / organization := "org.taymyr.lagom"
ThisBuild / version := "1.0.0"

// the Scala version that will be used for cross-compiled libraries
ThisBuild / scalaVersion := "2.13.6"

// Disable Cassandra and Kafka
ThisBuild / lagomCassandraEnabled := false
ThisBuild / lagomKafkaEnabled := false
ThisBuild / lagomServiceGatewayPort := 9010

val swaggerAnnotations = "io.swagger.core.v3" % "swagger-annotations" % "2.0.7"
val lagomOpenapiApi = "org.taymyr.lagom" %% "lagom-openapi-scala-api" % "1.3.0"
val lagomOpenapiImpl = "org.taymyr.lagom" %% "lagom-openapi-scala-impl" % "1.3.0"
val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.3" % Provided
val scalaTest = "org.scalatest" %% "scalatest" % "3.1.1" % Test

lazy val `lagom-openapi-scala-demo` = ( project in file( "." ) )
    .aggregate( `pets-api`, `pets-impl` )

lazy val `pets-api` = ( project in file( "pets-api" ) )
    .settings(
        libraryDependencies ++= Seq(
            lagomScaladslApi,
            swaggerAnnotations,
            lagomOpenapiApi
        )
    )

lazy val `vets-api` = ( project in file( "vets-api" ) )
    .settings(
        libraryDependencies ++= Seq(
            lagomScaladslApi,
            swaggerAnnotations,
            lagomOpenapiApi
        )
    )

lazy val `pets-impl` = ( project in file( "pets-impl" ) )
    .enablePlugins( LagomScala )
    .settings(
        libraryDependencies ++= Seq(
            macwire,
            lagomOpenapiImpl,
            lagomScaladslTestKit,
            scalaTest
        )
    )
    .settings( lagomServiceHttpPort := 9000 )
    .settings( lagomForkedTestSettings: _* )
    .dependsOn( `pets-api` )

lazy val `vets-impl` = ( project in file( "vets-impl" ) )
    .enablePlugins( LagomScala )
    .settings(
        libraryDependencies ++= Seq(
            macwire,
            lagomOpenapiImpl,
            lagomScaladslTestKit,
            scalaTest
        )
    )
    .settings( lagomServiceHttpPort := 9001 )
    .settings( lagomForkedTestSettings: _* )
    .dependsOn( `pets-api`, `vets-api` )

