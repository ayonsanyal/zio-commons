package com.ayon.config

import com.typesafe.config.Config
import zio.config.magnolia.descriptor
import zio.config.{ConfigSource, ReadError, read}
import zio.{IO, ZIO, ZLayer}

trait Config:
  val config: Config
  val configSource: ConfigSource

object Config:
  def getConfig = ZIO.serviceWith[Config](_.config)
  def getConfigSource = ZIO.serviceWith[Config](_.configSource)
