package com.ayon.commons.healthcheck

import zhttp.http.{Http, Request, Response}
import zio.ZIO

trait HealthCheckService:
  def startHealthCheck: ZIO[HealthCheckRoute, Throwable, Unit]

object HealthCheckService:
  def startHealthCheck = ZIO.serviceWithZIO[HealthCheckService](_.startHealthCheck)
