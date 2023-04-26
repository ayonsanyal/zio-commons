package com.ayon.commons.healthcheck

import zhttp.http.{Http, Request, Response}
import zio.ZIO

trait HealthCheckRoute:
  def status: Http[Any, Throwable, Request, Response]

object HealthCheckRoute:
  def status = ZIO.serviceWithZIO[HealthCheckRoute](s => ZIO.succeed(s.status))
