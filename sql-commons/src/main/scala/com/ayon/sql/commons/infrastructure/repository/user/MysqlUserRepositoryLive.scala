package com.ayon.sql.commons.infrastructure.repository.user

import com.ayon.sql.commons.domain.repository.user.UserRepository
import com.ayon.sql.entities.business.{Users}
import io.getquill.*
import zio.{ZEnvironment, ZIO, ZLayer}

import javax.sql.DataSource

case class MysqlUserRepositoryLive(dataSource: DataSource) extends MysqlZioJdbcContext(SnakeCase), UserRepository:
 
  import MysqlZioJdbcContext.*
  override def validateUserById(userId: Long) =
    run(
      query[Users]
        .filter(_.id == lift(userId))
        .map(_.id)
    )
      .map(_.nonEmpty)
      .provideEnvironment(ZEnvironment(dataSource))

object MysqlUserRepositoryLive:
  val live = ZLayer.fromFunction(MysqlUserRepositoryLive.apply(_))
