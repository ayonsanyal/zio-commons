package com.channelpilot.di.log

import zio.Runtime
import zio.logging.LogFormat
import zio.logging.backend.SLF4J

object LogSettings:
  val settings = Runtime.removeDefaultLoggers >>> SLF4J.slf4j(LogFormat.line)
