package com.channelpilot.di.sql.commons.domain.repository.translation

import zio.{IO, ZIO}

import java.sql.SQLException

trait TranslationRepository:

  def getTransactionsByKeyAndLocale: IO[SQLException, Map[String, String]]

object TranslationRepository:
  def getTransactionsByKeyAndLocale = ZIO.serviceWithZIO[TranslationRepository](_.getTransactionsByKeyAndLocale)
