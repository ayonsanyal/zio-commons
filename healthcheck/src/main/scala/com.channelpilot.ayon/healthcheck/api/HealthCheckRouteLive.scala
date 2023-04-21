package com.ayon.commons.healthcheck

import com.ayon.commons.healthcheck.{HealthCheckRoute}
import zhttp.http.*
import zio.*

case class HealthCheckRouteLive(
  
) extends HealthCheckRoute:

  def status = Http.collectZIO[Request] { case Method.GET -> !! / "status" =>
    for {

    } yield if statusBusiness && statusAnalytics then Response.ok else Response.status(Status.InternalServerError)
  }

object HealthCheckRouteLive:
  val live = ZLayer.fromFunction(HealthCheckRouteLive(_, _))
