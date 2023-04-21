package com.channelpilot.di.sql.commons.infrastructure.repository.channel


import com.channelpilot.di.sql.commons.domain.repository.channel.ChannelsRepository
import com.channelpilot.di.sql.entities.business.Channels
import io.getquill.*
import zio.{IO, ZEnvironment, ZIO, ZLayer}
import java.sql.SQLException
import javax.sql.DataSource

case class MysqlChannelsRepositoryLive(dataSource: DataSource) extends MysqlZioJdbcContext(SnakeCase), ChannelsRepository:

  override def getChannelName(channelId: Int) =
    run(
      query[Channels]
        .filter(channels => channels.id == lift(channelId))
        .map(_.title)
    )
      .map(_.headOption)
      .provideEnvironment(ZEnvironment(dataSource))


object MysqlChannelsRepositoryLive:
  val live = ZLayer.fromFunction(MysqlChannelsRepositoryLive.apply _)
