package com.ayon.commons

import com.channelpilot.di.commons.config.CPConfig
import zio.config.magnolia.descriptor
import zio.config.{ConfigSource, ReadError, read}
import zio.{IO, ZIO, ZLayer}

case class HealthCheckConfigServiceLive(config: Config) extends HealthCheckConfigService:
  override val getConfig = read(descriptor[HealthCheckConfig].from(config.configSource))

object HealthCheckConfigServiceLive:
  val live = ZLayer.fromFunction(HealthCheckConfigServiceLive.apply _)
