package com.channelpilot.di.sql.commons.domain.repository.shop

import zio.{Task, ZIO, IO}
import zio.stream.ZStream

import java.sql.SQLException

trait ShopsRepository:
  def checkShopsInactive(shopIds: Set[Int]): ZIO[Any, SQLException, List[Int]]
  def getUserIdByShop(shopId: Int): IO[SQLException, Long]
  def getCustomerId(shopId: Int): IO[SQLException, Int]
  def getLocale(shopId: Int): IO[SQLException, String]
  def validateShopById(shopId: Int): IO[SQLException, Boolean]

object ShopsRepository:
  def checkShopsInactive(shopIds: Set[Int]) = ZIO.serviceWithZIO[ShopsRepository](_.checkShopsInactive(shopIds))
  def getUserIdByShop(shopId: Int)          = ZIO.serviceWithZIO[ShopsRepository](_.getUserIdByShop(shopId))
  def getCustomerId(shopId: Int)            = ZIO.serviceWithZIO[ShopsRepository](_.getCustomerId(shopId))
  def getLocale(shopId: Int)                = ZIO.serviceWithZIO[ShopsRepository](_.getLocale(shopId))
  def validateShopById(shopId: Int)         = ZIO.serviceWithZIO[ShopsRepository](_.validateShopById(shopId))
