package com.channelpilot.di.sql.commons.domain.repository.channel

import zio.stream.ZStream
import zio.{IO, Task, ZIO}

import java.sql.SQLException

trait ChannelsRepository:

  def getChannelName(channelId: Int): IO[SQLException, Option[String]]

object ChannelsRepository:

  def getChannelName(channelId: Int) = ZIO.serviceWithZIO[ChannelsRepository](_.getChannelName(channelId))
