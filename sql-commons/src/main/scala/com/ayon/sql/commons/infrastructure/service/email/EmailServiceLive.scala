package com.channelpilot.di.sql.commons.infrastructure.service.email

import com.channelpilot.di.commons.domain.database.business.*
import zio.{Scope, ZEnvironment, ZIO, ZLayer}

import java.io.FileInputStream
import java.sql.SQLException
import java.time.LocalDateTime
import java.util.{Date, Properties, UUID}
import javax.activation.{DataHandler, FileDataSource}
import javax.mail.*
import javax.mail.internet.{InternetAddress, MimeBodyPart, MimeMessage, MimeMultipart}
import javax.mail.util.ByteArrayDataSource
import javax.sql.DataSource
import scala.sys.props
import scala.util.{Failure, Success, Try}
import com.channelpilot.di.sql.commons.domain.repository.email.MailRepository
import com.channelpilot.di.sql.commons.domain.repository.customer.CustomersRepository
import com.channelpilot.di.sql.commons.domain.service.email.EmailService
import com.channelpilot.di.sql.commons.domain.service.notification.EmailEvent
case class EmailServiceLive(mailContentsRepository: MailRepository, customersRepository: CustomersRepository) extends EmailService:

  override def createMessage(emailEvent: EmailEvent): ZIO[Any, Throwable, MimeMessage] =
    ZIO.scoped {
      for {
        props    <- getProperties
        message  <- ZIO.attempt(new MimeMessage(Session.getInstance(props)))
        locale   <- customersRepository.getLocale(emailEvent.customerId)
        content  <- mailContentsRepository.getMailContent(emailEvent.contentIdentifier, locale)
        template <- mailContentsRepository.getMailTemplate(emailEvent.templateIdentifier, locale)
        _ <- ZIO.logDebug(
               s"Email for ${emailEvent.contentIdentifier} fetched -> locale = $locale, content = ${content.subject}, template = ${template.text}"
             )
        _ <- ZIO.attempt {
               message.setSubject(getFinalText(content.subject, emailEvent.replace))
               message.setFrom(new InternetAddress(props.get("channelpilot.mail.from.address").toString, "Channel Pilot Solutions GmbH", "UTF-8"))
               message.setReplyTo(Array(new InternetAddress(props.get("channelpilot.mail.reply.address").toString)))
               message.setRecipients(Message.RecipientType.TO, emailEvent.emailTo.mkString(","))
               message.setRecipients(Message.RecipientType.BCC, props.get("channelpilot.mail.bcc.addresses").toString)
               message.setContent(getFinalContent(emailEvent, content, template))
               message.setSentDate(new Date())
             }
      } yield message
    }

  override def sendMessage(message: MimeMessage): ZIO[Any, Throwable, Unit] =
    ZIO.scoped {
      for {
        props <- getProperties
        _     <- ZIO.attempt(Transport.send(message, props.get("mail.smtp.user").toString, props.get("mail.password").toString))
        _     <- ZIO.logDebug(s"Email sent ${message.toString}")
      } yield ()
    }

  def getProperties: ZIO[Scope, Throwable, Properties] =
    for {
      stream <- ZIO.fromAutoCloseable(ZIO.attempt(getClass.getClassLoader.getResourceAsStream("config.properties")))
      props  <- ZIO.attempt(new Properties())
      _      <- ZIO.attempt(props.load(stream))
    } yield props

  def getFinalText(text: Option[String], replace: Map[String, String]) =
    if text.nonEmpty then replace.foldLeft(text.get)((acc, b) => acc.replace(s"[[${b._1}]]", b._2)) else ""

  def getFinalContent(emailEvent: EmailEvent, emailContent: MailContent, emailTemplate: MailTemplate) =
    val cover = new MimeMultipart("alternative")
    addCoverPart(cover, emailTemplate.html, emailContent.html, "text/html", emailEvent.replace)
    addCoverPart(cover, emailTemplate.text, emailContent.text, "text/plain", emailEvent.replace)
    val wrap = new MimeBodyPart()
    wrap.setContent(cover)
    val content = new MimeMultipart("related")
    content.addBodyPart(wrap)
    addAttachments(content, emailEvent.attachments)
    content

  def addCoverPart(cover: MimeMultipart, template: Option[String], content: Option[String], contentType: String, replace: Map[String, String]) =
    if template.nonEmpty && content.nonEmpty then
      cover.addBodyPart(getMailText(getFinalText(template, Map("content" -> getFinalText(content, replace))), contentType))

  def getMailText(content: String, contentType: String) =
    val mailText = new MimeBodyPart()
    mailText.setText(content, "UTF-8")
    mailText.setHeader("Content-Type", contentType + "; charset=\"utf-8\"")
    mailText.setHeader("Content-Transfer-Encoding", "quoted-printable");
    mailText

  def addAttachments(content: MimeMultipart, attachments: List[(String, String)]) =
    attachments.map { file => // file name, file content
      val attachment     = new MimeBodyPart()
      val fileDataSource = new ByteArrayDataSource(file._2, "text/csv")
      attachment.setDataHandler(new DataHandler(fileDataSource))
      attachment.setHeader("Content-ID", s"<${UUID.randomUUID()}>")
      attachment.setFileName(file._1)
      content.addBodyPart(attachment)
    }

object EmailServiceLive:
  val live = ZLayer.fromFunction(EmailServiceLive(_, _))
