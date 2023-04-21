package com.channelpilot.di.sql.commons.infrastructure.service.notification

import com.channelpilot.di.commons.domain.database.business.{EventDefinition, EventDefinitionsRepository, LogbookEntry, LogbookRepository}
import com.channelpilot.di.commons.domain.{EmailEvent, EmailService, LogbookEvent, NotificationService}
import io.getquill.*
import zio.json.*
import zio.{ZEnvironment, ZIO, ZLayer}

import java.sql.SQLException
import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, Session, Transport}
import javax.sql.DataSource

case class NotificationServiceLive(
  eventDefinitionsRepository: EventDefinitionsRepository,
  logbookRepository: LogbookRepository,
  emailService: EmailService
) extends NotificationService:

  private case class LogbookMsg(eventKey: String, messageParams: List[String])
  private given JsonEncoder[LogbookMsg] = DeriveJsonEncoder.gen[LogbookMsg]
  // Expect failure if eventDefinition can't be fetched or logbook entry can't be saved
  override def handle(logbookEvent: LogbookEvent) =
    val data = Map("msg" -> LogbookMsg(logbookEvent.eventId, logbookEvent.params).toJson)
    for {
      eventDefinition <- eventDefinitionsRepository.getDefinition(logbookEvent.eventId)
      entry <- ZIO.attempt(
                 LogbookEntry(
                   customerId = logbookEvent.customerId,
                   channelId = logbookEvent.channelId,
                   data = Some(
                     if eventDefinition.nonEmpty then
                       data ++ Map(
                         "lvl"  -> eventDefinition.head.level,
                         "comp" -> eventDefinition.head.logbookComponent.getOrElse("")
                       )
                     else data
                   ),
                   shopId = logbookEvent.shopId,
                   userId = logbookEvent.userId
                 )
               )
      _ <- logbookRepository.saveEntry(entry)
    } yield ()

  override def handle(emailEvent: EmailEvent) =
    for {
      message <- emailService.createMessage(emailEvent)
      _       <- emailService.sendMessage(message)
    } yield ()

object NotificationServiceLive:
  val live = ZLayer.fromFunction(NotificationServiceLive(_, _, _))
