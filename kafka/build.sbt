enablePlugins(ChannelPilotPlugin)

Global / logLevel     := Level.Debug
Global / scalaVersion := "3.2.0"

lazy val root = (project in file("."))
  .settings(
    name       := "di-kafka",
    maintainer := "data-intelligence@channelpilot.com",
    libraryDependencies ++= Seq(
      "com.channelpilot" % "di-config" % "2023-02-10.4",
      "dev.zio"         %% "zio-kafka" % "2.0.1",
      "dev.zio"         %% "zio-json"  % "0.3.0"
    )
  )
