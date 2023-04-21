package com.channelpilot.di.sql.commons.domain.repository.event

import zio.ZIO

import java.sql.SQLException

trait EventDefinitionsRepository:
  def getDefinition(eventId: String): ZIO[Any, SQLException, List[EventDefinition]]

object EventDefinitionsRepository:
  def getDefinition(eventId: String) = ZIO.serviceWithZIO[EventDefinitionsRepository](_.getDefinition(eventId))
