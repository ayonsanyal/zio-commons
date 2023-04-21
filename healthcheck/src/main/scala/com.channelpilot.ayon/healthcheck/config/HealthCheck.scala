package com.ayon.commons.http.healthcheck.config

case class HealthCheck(host: String, port: Int)
case class HealthCheckConfig(healthCheck: HealthCheck)
