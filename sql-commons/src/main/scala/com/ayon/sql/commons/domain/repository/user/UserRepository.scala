package com.ayon.sql.commons.domain.repository.user

import zio.{IO, *}

import java.sql.SQLException

trait UserRepository:
  def validateUserById(userId: Long): IO[SQLException, Boolean]

object UserRepository:
  def validateUserById(userId: Long)             = ZIO.serviceWithZIO[UserRepository](_.validateUserById(userId))
