package com.ayon.cassandra.domain.entities.event

import java.time.{Instant, LocalDate, LocalDateTime, ZoneId}
import java.util.UUID

case class LogbookEntry(
  customerId: Int,
  typ: String = "log",
  createDate: Instant = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant,
  createdHour: Int = LocalDateTime.now().getHour,
  epochMilli: BigInt = Instant.now().toEpochMilli,
  uuid: UUID = UUID.randomUUID(),
  channelId: Option[Int] = None,
  data: Option[Map[String, String]] = None,
  shopId: Option[Int] = None,
  userId: Option[BigInt] = None
)
