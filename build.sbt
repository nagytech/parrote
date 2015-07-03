name := "parrote"

version := "1.0"

scalaVersion := "2.11.6"

lazy val root = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)

libraryDependencies += "org.webjars" % "bootstrap" % "3.3.5"