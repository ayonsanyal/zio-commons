package com.ayon.kafka.service

import com.channelpilot.di.commons.domain.Finished
import com.channelpilot.di.commons.kafka.config.Kafka
import org.apache.kafka.clients.producer.ProducerRecord
import zio.kafka.producer.Producer
import com.channelpilot.di.commons.kafka.serde.ZIOKafkaSerdeProvider.*
import zio.*
import zio.json.JsonEncoder

case class KafkaProducerServiceLive(producer: Producer) extends ProducerService:

  def produce[T <: AnyRef](topic: String, key: String, value: T)(implicit encoder: JsonEncoder[T]) =
    for {
      record   <- ZIO.attempt(new ProducerRecord(topic, key, value))
      serKey   <- kafkaZIOKeySerializer
      serValue <- kafkaZIOValueSerializer[T]
      _        <- producer.produce(record, keySerializer = serKey, valueSerializer = serValue)
      _        <- ZIO.logDebug(s"Produced the message : $value to topic: ${topic}")
    } yield Finished

object KafkaProducerServiceLive:
  val live = ZLayer.fromFunction(KafkaProducerServiceLive(_))
