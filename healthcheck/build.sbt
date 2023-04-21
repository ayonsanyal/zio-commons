enablePlugins(BuildEnvPlugin)

Global / logLevel     := Level.Debug
Global / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name       := "healthcheck",
    maintainer := "ayon.sanyal@gmail.com",
    libraryDependencies ++= Seq(
      "com.ayon" % "config" % "2022-10-27.1",
      "io.d11" %% "zhttp" % "2.0.0-RC11",
      "io.d11" %% "zhttp-test" % "2.0.0-RC9" % Test,
    )
  )
