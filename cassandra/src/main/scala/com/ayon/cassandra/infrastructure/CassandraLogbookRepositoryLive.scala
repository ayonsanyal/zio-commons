package com.ayon.cassandra.infrastructure

import com.ayon.cassandra.domain.entities.event.{ LogbookEntry}
import com.ayon.cassandra.domain.entities.repository.LogbookRepository
import io.getquill.*
import zio.{ZEnvironment, ZIO, ZLayer}

import java.time.{Instant, LocalDateTime, ZoneOffset}
import java.util.UUID

case class CassandraLogbookRepositoryLive(dataSource: CassandraZioSession) extends CassandraZioContext(SnakeCase), LogbookRepository:

  override def saveEntry(logbookEntry: LogbookEntry) =
    run(
      query[Event]
        .insert(
          _.cstm  -> lift(logbookEntry.customerId),
          _.typ   -> lift(logbookEntry.typ),
          _.date  -> lift(logbookEntry.createDate),
          _.hour  -> lift(logbookEntry.createdHour),
          _.time  -> lift(logbookEntry.epochMilli.toLong),
          _.uuid  -> lift(logbookEntry.uuid),
          _.chnnl -> lift(logbookEntry.channelId),
          _.data  -> lift(logbookEntry.data),
          _.shop  -> lift(logbookEntry.shopId),
          _.user  -> lift(logbookEntry.userId.map(_.toLong))
        )
        .ifNotExists
    ).provideEnvironment(ZEnvironment(dataSource))

object CassandraLogbookRepositoryLive:
  val live = ZLayer.fromFunction(CassandraLogbookRepositoryLive.apply _)
