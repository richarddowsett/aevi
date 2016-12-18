name := """aevi"""
organization := "aevi"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies += filters
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "2.3.5" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "aevi.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "aevi.binders._"
