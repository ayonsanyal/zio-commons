package com.ayon.config

import com.typesafe.config.ConfigFactory
import zio.config.ConfigSource
import zio.config.typesafe.TypesafeConfigSource
import zio.{ZIO, ZLayer}

case class ConfigLive() extends Config:
  override val config =
    Option(System.getProperty("conf.dir")) match {
      case Some(value) if value.nonEmpty =>
        ConfigFactory.parseFile(new java.io.File(s"$value/application.conf")).resolve().withFallback(ConfigFactory.load().resolve())
      case _ => ConfigFactory.load().resolve()
    }

  override val configSource = TypesafeConfigSource.fromTypesafeConfig(ZIO.attempt(config))

object ConfigLive:
  val live = ZLayer.fromFunction(ConfigLive.apply _)
