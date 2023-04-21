package com.channelpilot.di.sql.commons.infrastructure.repository.email

import com.channelpilot.di.sql.commons.domain.repository.email.{MailContent, MailRepository, MailTemplate}
import com.channelpilot.di.sql.entities.business.{I18ns, L10ns, MailContents, MailTemplates}
import io.getquill.*
import zio.{IO, ZEnvironment, ZIO, ZLayer}

import java.sql.SQLException
import javax.sql.DataSource

case class MysqlMailRepositoryLive(dataSource: DataSource) extends MysqlZioJdbcContext(SnakeCase), MailRepository:

  override def getMailContent(identifier: String, locale: String): ZIO[Any, SQLException, MailContent] =
    for {
      result <- run(
                  query[MailContents]
                    .filter(content => content.identifier == lift(identifier))
                    .take(1)
                    .map(content => (content.html, content.text, content.subject))
                ).provideEnvironment(ZEnvironment(dataSource))
      html    <- getTranslation(result.headOption.flatMap(_._1), locale)
      text    <- getTranslation(result.headOption.flatMap(_._2), locale)
      subject <- getTranslation(result.headOption.flatMap(_._3), locale)
    } yield {
      MailContent(
        identifier = identifier,
        html = html.headOption.flatten,
        text = text.headOption.flatten,
        subject = subject.headOption.flatten
      )
    }

  override def getMailTemplate(identifier: String, locale: String): ZIO[Any, SQLException, MailTemplate] =
    for {
      result <- run(
                  query[MailTemplates]
                    .filter(template => template.identifier == lift(identifier))
                    .take(1)
                    .map(template => (template.html, template.text))
                ).provideEnvironment(ZEnvironment(dataSource))
      html <- getTranslation(result.headOption.flatMap(_._1), locale)
      text <- getTranslation(result.headOption.flatMap(_._2), locale)
    } yield {
      MailTemplate(
        identifier = identifier,
        html = html.headOption.flatten,
        text = text.headOption.flatten
      )
    }

  def getTranslation(id: Option[Long], locale: String) = if id.isEmpty then ZIO.succeed(Seq.empty)
  else
    run(
      query[I18ns]
        .filter(_.id == lift(id.get))
        .leftJoin(query[L10ns].filter(_.locale == lift(locale)))
        .on((i, l) => i.id == l.i18nId)
        .map(_._2.map(_.l10nValue))
    ).provideEnvironment(ZEnvironment(dataSource))

object MysqlMailRepositoryLive:
  val live = ZLayer.fromFunction(MysqlMailRepositoryLive.apply _)
