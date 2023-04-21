package com.channelpilot.di.sql.commons.domain.service.translation

import zio.*

trait TranslationService:
  def getTranslationsByLocaleAndKey(key: String, locale: String): UIO[String]

object TranslationService:
  def getTranslationsByLocaleAndKey(key: String, locale: String) =
    ZIO.serviceWithZIO[TranslationService](_.getTranslationsByLocaleAndKey(key, locale))
