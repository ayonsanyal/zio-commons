package com.channelpilot.di.sql.commons.domain.repository.customer

import zio.stream.ZStream
import zio.{IO, Task, ZIO}

import java.sql.SQLException

trait CustomersRepository:
  def getLocale(customerId: Int): IO[SQLException, String]

object CustomersRepository:
  def getLocale(customerId: Int) = ZIO.serviceWithZIO[CustomersRepository](_.getLocale(customerId))
