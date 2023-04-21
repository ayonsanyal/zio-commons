package com.ayon.kafka.service

import com.channelpilot.di.commons.domain.Finished
import zio.ZIO
import zio.json.JsonEncoder

trait ProducerService:
  def produce[T <: AnyRef](topic: String, key: String, value: T)(implicit encoder: JsonEncoder[T]): ZIO[Any, Throwable, Finished.type]

object ProducerService:
  def produce[T <: AnyRef](topic: String, key: String, value: T)(implicit encoder: JsonEncoder[T]) =
    ZIO.serviceWithZIO[ProducerService](producerService => producerService.produce(topic, key, value))
