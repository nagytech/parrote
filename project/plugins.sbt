resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.2")

// Coffeescript compilation
addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.0")

// Less compilation
addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.0.6")

// JSHint
addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.3")

// RequireJS
addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.7")

// Ebean
addSbtPlugin("com.typesafe.sbt" % "sbt-play-ebean" % "1.0.0")
