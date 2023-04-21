package com.channelpilot.di.cassandra.domain.entities.repository

import com.channelpilot.di.cassandra.domain.entities.event.LogbookEntry
import zio.ZIO

trait LogbookRepository:
  def saveEntry(logbookEntry: LogbookEntry): ZIO[Any, Throwable, Unit]

object LogbookRepository:
  def saveEntry(logbookEntry: LogbookEntry) = ZIO.serviceWithZIO[LogbookRepository](_.saveEntry(logbookEntry))
