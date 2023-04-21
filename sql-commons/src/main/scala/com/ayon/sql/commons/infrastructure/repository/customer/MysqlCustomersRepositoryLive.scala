package com.channelpilot.di.sql.commons.infrastructure.repository.customer

import com.channelpilot.di.commons.domain.database.business.CustomersRepository
import com.channelpilot.di.sql.entities.business.{Customers, Shops, Users}
import io.getquill.*
import org.apache.kafka.common.serialization.Serdes.UUID
import zio.{IO, ZEnvironment, ZIO, ZLayer}

import java.sql.SQLException
import javax.sql.DataSource

case class MysqlCustomersRepositoryLive(dataSource: DataSource) extends MysqlZioJdbcContext(SnakeCase), CustomersRepository:

  override def getLocale(customerId: Int): IO[SQLException, String] =
    run(query[Customers].filter(customer => customer.id == lift(customerId)).map(_.locale))
      .map(data => if data.nonEmpty then data.head else "en")
      .provideEnvironment(ZEnvironment(dataSource))

object MysqlCustomersRepositoryLive:
  val live = ZLayer.fromFunction(MysqlCustomersRepositoryLive.apply _)
