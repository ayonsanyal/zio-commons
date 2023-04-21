enablePlugins(ChannelPilotPlugin)

Global / logLevel     := Level.Debug
Global / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name       := "sql-commons",
    maintainer := "ayon.sanyal@gmail.com",
    libraryDependencies ++= Seq(
      "ch.qos.logback"         % "logback-classic"              % "1.4.4",
      "mysql"                  % "mysql-connector-java"         % "8.0.31",
      "io.getquill"           %% "quill-jdbc-zio"               % "4.6.0",
      "io.getquill"           %% "quill-jdbc"                   % "4.6.0",
      "io.github.scottweaver" %% "zio-2-0-testcontainers-mysql" % "0.9.0",
      "io.github.scottweaver" %% "zio-2-0-db-migration-aspect"  % "0.9.0",
      "com.channelpilot"       % "config"                    % "2023-02-10.4"
    )
  )
