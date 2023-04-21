package com.channelpilot.di.cassandra.domain.entities.event

import java.time.Instant
import java.util.UUID

case class Event(
                  cstm: Int,
                  typ: String,
                  date: Instant,
                  hour: Int,
                  time: Long,
                  uuid: UUID,
                  chnnl: Option[Int],
                  data: Option[Map[String, String]],
                  shop: Option[Int],
                  user: Option[Long]
                )
