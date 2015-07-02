name := "parrote"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies += "org.webjars" % "bootstrap" % "3.3.4"

pipelineStages := Seq(rjs)