package com.channelpilot.di.sql.commons.infrastructure.repository.event

import com.channelpilot.di.sql.commons.domain.repository.event.{EventDefinition, EventDefinitionsRepository}
import com.channelpilot.di.sql.entities.business.EventDefinitions
import io.getquill.*
import zio.{IO, ZEnvironment, ZIO, ZLayer}

import java.sql.SQLException
import javax.sql.DataSource

case class MysqlEventDefinitionsRepositoryLive(dataSource: DataSource) extends MysqlZioJdbcContext(SnakeCase), EventDefinitionsRepository:

  override def getDefinition(eventId: String) =
    run(
      query[EventDefinitions]
        .filter(definition => lift(eventId).equals(definition.eventId))
        .take(1)
        .map(row => EventDefinition(row.eventId, row.level, row.logbookComponent))
    ).provideEnvironment(ZEnvironment(dataSource))

object MysqlEventDefinitionsRepositoryLive:
  val live = ZLayer.fromFunction(MysqlEventDefinitionsRepositoryLive.apply _)
