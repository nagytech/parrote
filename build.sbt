name := "parrote"

version := "1.0"

scalaVersion := "2.11.1"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies += "org.webjars" % "bootstrap" % "3.0.2"