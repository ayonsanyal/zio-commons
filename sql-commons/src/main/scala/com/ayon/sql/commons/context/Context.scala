package com.ayon.sql.commons.context

import com.ayon.config.Config
import io.getquill.context.ZioJdbc.DataSourceLayer
import io.getquill.JdbcContextConfig
import zio.ZLayer

object Context:
  def jdbcContext(prefix: String) = ZLayer(for {
    config <- Config.getConfig
  } yield JdbcContextConfig(config.getConfig(prefix)).dataSource)
