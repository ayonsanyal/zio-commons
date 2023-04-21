package com.channelpilot.di.sql.commons.infrastructure.repository.translation

import com.channelpilot.di.commons.domain.translation.TranslationRepository
import com.channelpilot.di.sql.entities.business.{I18ns, L10ns, Translations}
import io.getquill.*
import zio.stream.*
import zio.{IO, ZEnvironment, ZIO, ZLayer}

import java.sql.SQLException
import javax.sql.DataSource

case class MysqlTranslationsRepositoryLive(dataSource: DataSource) extends MysqlZioJdbcContext(SnakeCase), TranslationRepository:

  import MysqlZioJdbcContext.*
  def getTransactionsByKeyAndLocale: IO[SQLException, Map[String, String]] =
    run(
      for {
        translation <- query[Translations]
        i18         <- query[I18ns].filter(_.id == translation.value)
        l10         <- query[L10ns].filter(_.i18nId == i18.id)
      } yield (translation.translationKey, l10.locale, l10.l10nValue)
    )
      .map(_.map(result => (s"${result._2}-${result._1}", result._3)).toMap)
      .provideEnvironment(ZEnvironment(dataSource))

object MysqlTranslationsRepositoryLive:
  val live = ZLayer.fromFunction(MysqlTranslationsRepositoryLive.apply _)
