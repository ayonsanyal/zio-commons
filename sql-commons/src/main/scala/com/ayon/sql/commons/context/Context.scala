package com.channelpilot.di.sql.commons.context

import com.channelpilot.di.config.CPConfig
import io.getquill.context.ZioJdbc.DataSourceLayer
import io.getquill.JdbcContextConfig
import zio.ZLayer

object Context:
  def jdbcContext(prefix: String) = ZLayer(for {
    config <- CPConfig.getCPConfig
  } yield JdbcContextConfig(config.getConfig(prefix)).dataSource)
