enablePlugins(BuildEnvPlugin)

Global / logLevel := Level.Debug
Global / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name := "cassandra",
    maintainer := "ayon.sanyal@gmail.com",
    libraryDependencies ++= Seq(
      "com.ayon" % "config" % "2023-02-10.4",
      "io.getquill" %% "quill-cassandra" % "4.6.0",
      "io.getquill" %% "quill-cassandra-zio" % "4.6.0"
    )
  )
