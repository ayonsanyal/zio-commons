import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

object BuildEnvPlugin extends AutoPlugin {
  override def trigger = AllRequirements

  override def requires = JvmPlugin

  object autoImport {
    object BuildEnv extends Enumeration {
      val Production, Staging, Test, Development = Value
    }

    val buildEnv = settingKey[BuildEnv.Value]("the current build environment")
  }

  import autoImport._

  override def projectSettings = Seq(
    buildEnv := {
      sys.props.get("env")
        .orElse(sys.env.get("ENV"))
        .flatMap {
          case "production" => Some(BuildEnv.Production)
          case "staging" => Some(BuildEnv.Staging)
          case "test" => Some(BuildEnv.Test)
          case "dev" => Some(BuildEnv.Development)
          case unknown => None
        }
        .getOrElse(BuildEnv.Development)
    },
    onLoadMessage := {
      val defaultMessage = onLoadMessage.value // depends on the old message.
      val env = buildEnv.value
      s"""|$defaultMessage
          |Running in build environment: $env""".stripMargin
    }
  )
}
