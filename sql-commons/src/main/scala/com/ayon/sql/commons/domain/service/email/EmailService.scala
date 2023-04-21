package com.channelpilot.di.sql.commons.domain.service.email

import zio.ZIO
import javax.mail.internet.MimeMessage

trait EmailService:

  def createMessage(emailEvent: EmailEvent): ZIO[Any, Throwable, MimeMessage]

  def sendMessage(message: MimeMessage): ZIO[Any, Throwable, Unit]

object EmailService:

  def createMessage(emailEvent: EmailEvent) = ZIO.serviceWithZIO[EmailService](_.createMessage(emailEvent))

  def sendMessage(message: MimeMessage) = ZIO.serviceWithZIO[EmailService](_.sendMessage(message))
