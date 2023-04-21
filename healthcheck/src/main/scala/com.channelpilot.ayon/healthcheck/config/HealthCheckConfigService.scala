package com.ayon.commons.http.healthcheck.config

import zio.config.ReadError
import zio.{IO, ZIO, ZLayer}

trait HealthCheckConfigService:
  val getConfig: IO[ReadError[String], HealthCheckConfig]

object HealthCheckConfigService:
  def getHealthCheckConfig() = ZIO.serviceWithZIO[HealthCheckConfigService](_.getConfig)
