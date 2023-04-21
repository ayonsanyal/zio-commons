package com.channelpilot.di.cassandra.context

import com.channelpilot.di.config.CPConfig
import io.getquill.{CassandraContextConfig, CassandraZioSession}
import zio.ZLayer

object Context:
  def cassandraContext(prefix: String) = ZLayer(for {
    config <- CPConfig.getCPConfig
  } yield CassandraContextConfig(config.getConfig(prefix))) >>> CassandraZioSession.live
