package com.channelpilot.di.sql.commons.infrastructure.repository.shop

import com.channelpilot.di.sql.commons.domain.repository.shop.ShopsRepository
import com.channelpilot.di.sql.entities.business.{Customers, Shops, Users}
import io.getquill.*
import zio.{IO, ZEnvironment, ZIO, ZLayer}

import java.sql.SQLException
import javax.sql.DataSource

case class MysqlShopsRepositoryLive(dataSource: DataSource) extends MysqlZioJdbcContext(SnakeCase), ShopsRepository:

  override def checkShopsInactive(shopIds: Set[Int]) =
    run(
      query[Shops]
        .filter(shop => liftQuery(shopIds).contains(shop.id))
        .join(query[Customers])
        .on((shop, customer) => shop.customerId == customer.id)
        .filter((shop, customer) => !shop.isActive || shop.isDeleted || customer.isDeactivated)
        .map(_._1.id)
    ).provideEnvironment(ZEnvironment(dataSource))

  override def getUserIdByShop(shopId: Int) =
    run(
      for {
        shop <- query[Shops]
        user <- query[Users].filter(_.customerId == shop.customerId && shop.id == lift(shopId))
      } yield user.id
    )
      .map(data => if data.nonEmpty then data.head else 0)
      .provideEnvironment(ZEnvironment(dataSource))

  override def getCustomerId(shopId: Int) =
    run(
      query[Shops]
        .filter(_.id == lift(shopId))
        .map(_.customerId)
    )
      .map(data => if data.nonEmpty then data.head else 0)
      .provideEnvironment(ZEnvironment(dataSource))

  override def getLocale(shopId: Int) =
    run(
      for {
        shop      <- query[Shops].filter(shop => shop.id == lift(shopId))
        customers <- query[Customers].filter(customer => customer.id == shop.customerId)
      } yield (customers.locale)
    )
      .map(data => if data.nonEmpty then data.head else "en")
      .provideEnvironment(ZEnvironment(dataSource))

  override def validateShopById(shopId: Int) =
    run(
      query[Shops]
        .filter(_.id == lift(shopId))
        .map(_.id)
    )
      .map(_.nonEmpty)
      .provideEnvironment(ZEnvironment(dataSource))

object MysqlShopsRepositoryLive:
  val live = ZLayer.fromFunction(MysqlShopsRepositoryLive.apply _)
