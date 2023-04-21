package com.channelpilot.di.sql.commons.infrastructure.service.translation

import com.channelpilot.di.commons.domain.translation.TranslationService
import zio.*

case class TranslationServiceLive(translationCache: TranslationSettings) extends TranslationService:
  def getTranslationsByLocaleAndKey(key: String, locale: String) = ZIO.succeed(
    translationCache.translations.getOrElse(s"$locale-$key", "")
  )

object TranslationServiceLive:
  val live = ZLayer.fromFunction(TranslationServiceLive(_))
