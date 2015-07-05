name := "parrote"

version := "1.0"

scalaVersion := "2.11.6"

lazy val root = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)

libraryDependencies ++= Seq(
  "org.webjars" % "bootstrap" % "3.3.5",
  "org.webjars" % "jquery" % "1.11.3",
  "org.mindrot" % "jbcrypt" % "0.3m")