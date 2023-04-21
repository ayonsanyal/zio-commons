package com.channelpilot.di.sql.commons.domain.repository.email

case class MailContent(
                        identifier: String,
                        html: Option[String],
                        text: Option[String],
                        subject: Option[String]
                      )

case class MailTemplate(
                         identifier: String,
                         html: Option[String],
                         text: Option[String]
                       )