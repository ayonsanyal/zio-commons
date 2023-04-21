package com.ayon.cassandra.context

import com.ayon.config.Config
import io.getquill.{CassandraContextConfig, CassandraZioSession}
import zio.ZLayer

object Context:
  def cassandraContext(prefix: String) = ZLayer(for {
    config <- Config.getConfig
  } yield CassandraContextConfig(config.getConfig(prefix))) >>> CassandraZioSession.live
