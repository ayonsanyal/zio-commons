package com.channelpilot.di.sql.commons.domain.repository.email

import zio.stream.ZStream
import zio.ZIO

import java.sql.SQLException

trait MailRepository:
  def getMailContent(identifier: String, locale: String): ZIO[Any, SQLException, MailContent]

  def getMailTemplate(identifier: String, locale: String): ZIO[Any, SQLException, MailTemplate]

object MailRepository:
  def getMailContent(identifier: String, locale: String) = ZIO.serviceWithZIO[MailRepository](_.getMailContent(identifier, locale))

  def getMailTemplate(identifier: String, locale: String) = ZIO.serviceWithZIO[MailRepository](_.getMailTemplate(identifier, locale))
