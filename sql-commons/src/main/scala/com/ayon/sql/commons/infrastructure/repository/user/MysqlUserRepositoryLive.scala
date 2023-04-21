package com.channelpilot.di.sql.commons.infrastructure.repository.user

import com.channelpilot.di.sql.commons.domain.repository.user.UserRepository
import com.channelpilot.di.sql.entities.business.{Shops, Users}
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

  override def validateAdminOrPMById(userId: Long) =
    run(
      query[Users]
        .filter(_.id == lift(userId))
    )
      .map(users => users.nonEmpty && users.exists(user => user.isChannelpilotAdmin && user.isChannelpilotProductManager))
      .provideEnvironment(ZEnvironment(dataSource))

  override def hasAccessToShop(userId: Long, shopId: Int) =
    run(
      query[Users]
        .filter(_.id == lift(userId))
        .join(query[Shops].filter(_.id == lift(shopId)))
        .on((user, shop) => user.customerId == shop.customerId)
        .map(_._1.id)
    )
      .map(_.nonEmpty)
      .provideEnvironment(ZEnvironment(dataSource))

  override def getLocale(userId: Long) =
    run(query[Users].filter(_.id == lift(userId)).map(_.locale))
      .map(data => if data.nonEmpty then data.head else "en")
      .provideEnvironment(ZEnvironment(dataSource))

object MysqlUserRepositoryLive:
  val live = ZLayer.fromFunction(MysqlUserRepositoryLive.apply(_))
