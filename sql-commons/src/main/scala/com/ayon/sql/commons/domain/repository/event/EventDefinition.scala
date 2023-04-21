package com.channelpilot.di.sql.commons.domain.repository.event

case class EventDefinition(
  eventId: String,
  level: String,
  logbookComponent: Option[String]
)
