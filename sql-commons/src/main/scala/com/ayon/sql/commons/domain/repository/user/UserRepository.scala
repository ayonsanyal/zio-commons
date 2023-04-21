package com.channelpilot.di.sql.commons.domain.repository.user

import zio.{IO, *}

import java.sql.SQLException

trait UserRepository:
  def validateUserById(userId: Long): IO[SQLException, Boolean]
  def validateAdminOrPMById(userId: Long): IO[SQLException, Boolean]
  def hasAccessToShop(userId: Long, shopId: Int): IO[SQLException, Boolean]
  def getLocale(userId: Long): IO[SQLException, String]

object UserRepository:
  def validateUserById(userId: Long)             = ZIO.serviceWithZIO[UserRepository](_.validateUserById(userId))
  def validateAdminOrPMById(userId: Long)        = ZIO.serviceWithZIO[UserRepository](_.validateAdminOrPMById(userId))
  def hasAccessToShop(userId: Long, shopId: Int) = ZIO.serviceWithZIO[UserRepository](_.hasAccessToShop(userId, shopId))
  def getLocale(userId: Long)                    = ZIO.serviceWithZIO[UserRepository](_.getLocale(userId))
