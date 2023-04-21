enablePlugins(BuildEnvPlugin)

Global / logLevel := Level.Debug
Global / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name := "kafka",
    maintainer := "ayon.sanyal@gmail.com",
    libraryDependencies ++= Seq(
      "com.ayon" % "config" % "2023-02-10.4",
      "dev.zio" %% "zio-kafka" % "2.0.1",
      "dev.zio" %% "zio-json" % "0.3.0"
    )
  )
