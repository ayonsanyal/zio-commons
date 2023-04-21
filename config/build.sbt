enablePlugins(BuildEnvPlugin)

Global / logLevel     := Level.Debug
Global / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name       := "config",
    maintainer := "ayon.sanyal@gmail.com",
    libraryDependencies ++= Seq(
      "dev.zio"       %% "zio-config"            % "3.0.7",
      "dev.zio"       %% "zio-config-typesafe"   % "3.0.7",
      "dev.zio"       %% "zio-config-magnolia"   % "3.0.7",
      "dev.zio"       %% "zio-config-derivation" % "3.0.7",
      "dev.zio"       %% "zio"                   % "2.0.2",
      "dev.zio"       %% "zio-logging"           % "2.1.2",
      "dev.zio"       %% "zio-logging-slf4j"     % "2.1.2",
      "dev.zio"       %% "zio-streams"           % "2.0.2",
      "dev.zio"       %% "zio-test"              % "2.0.2",
      "ch.qos.logback" % "logback-classic"       % "1.4.4"
    )
  )
