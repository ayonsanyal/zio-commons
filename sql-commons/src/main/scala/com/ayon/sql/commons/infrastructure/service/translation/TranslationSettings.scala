package com.channelpilot.di.sql.commons.infrastructure.service.translation

import com.channelpilot.di.commons.domain.translation.TranslationRepository
import zio.*

import java.sql.SQLException

case class TranslationSettings(
  translations: Map[String, String]
)

object TranslationSettings:

  def translationCache = ZIO.serviceWith[TranslationSettings](_.translations)

  val live = ZLayer {
    for {
      translationCache: Map[String, String] <- TranslationRepository.getTransactionsByKeyAndLocale
    } yield TranslationSettings(translationCache)
  }
