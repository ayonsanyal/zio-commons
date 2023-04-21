package com.channelpilot.di.sql.commons.domain.service.notification

import zio.ZIO

trait NotificationService:
  def handle(logbookEvent: LogbookEvent): ZIO[Any, Throwable, Unit]

  def handle(emailEvent: EmailEvent): ZIO[Any, Throwable, Unit]

object NotificationService:
  def handle(logbookEvent: LogbookEvent) = ZIO.serviceWithZIO[NotificationService](_.handle(logbookEvent))

  def handle(emailEvent: EmailEvent) = ZIO.serviceWithZIO[NotificationService](_.handle(emailEvent))

case class LogbookEvent(
  eventId: String,
  customerId: Int,
  channelId: Option[Int] = None,
  params: List[String] = List.empty[String],
  shopId: Option[Int] = None,
  userId: Option[BigInt] = None
)

case class EmailEvent(
  shopId: Int,
  customerId: Int,
  emailTo: List[String],
  templateIdentifier: String, // default | empty
  contentIdentifier: String,  // eg. mail.priceAnalytics.requested
  replace: Map[String, String] = Map.empty[String, String],
  attachments: List[(String, String)] = Nil // (file name, file content)
)
