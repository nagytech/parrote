name := "parrote"

version := "1.0"

scalaVersion := "2.11.6"

lazy val root = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)

includeFilter in (Assets, LessKeys.less) := "*.less"

libraryDependencies ++= Seq(
  "org.webjars" % "bootstrap" % "3.3.5",
  "org.webjars" % "jquery" % "1.11.3",
  "org.webjars" % "momentjs" % "2.9.0",
  "org.webjars" % "modernizr" % "2.8.3",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.mongodb" % "mongo-java-driver" % "3.0.3",
  "org.jongo" % "jongo" % "1.2")
