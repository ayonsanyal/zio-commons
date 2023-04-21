package com.ayon.kafka.service

import com.channelpilot.di.commons.domain.Finished
import com.channelpilot.di.commons.kafka.KafkaDependencies.*
import com.channelpilot.di.commons.kafka.config.Kafka
import com.channelpilot.di.commons.kafka.serde.ZIOKafkaSerdeProvider.{kafkaDeSerializer, kafkaSerde}
import org.apache.kafka.common.serialization.Deserializer
import zio.*
import zio.json.{JsonDecoder, JsonEncoder}
import zio.kafka.consumer.{CommittableRecord, Consumer, Offset, Subscription}
import zio.kafka.producer.Producer
import zio.kafka.serde.Serde
import zio.stream.ZStream

case class KafkaConsumerServiceLive(consumer: Consumer) extends ConsumerService:

  override def consume[T <: AnyRef](topic: List[String])(implicit decoder: JsonDecoder[T], encoder: JsonEncoder[T]) =
    ZIO.attempt(
      consumer
        .subscribeAnd(Subscription.Topics(topic.toSet))
        .plainStream(Serde.string, kafkaSerde[T](decoder, encoder))
    )

  override def stop = consumer.stopConsumption *> consumer.unsubscribe

  override def commitOffsetBatches(topic: String, messageStream: ZIOKafkaStreamOffset) =
    messageStream
      .run(Consumer.offsetBatches)
      .flatMap(_.commit)
      .<*(ZIO.logDebug(s"Consumer flow finished for topic: $topic and all the messages are committed"))
      .as(Finished)

object KafkaConsumerServiceLive:
  val live = ZLayer.fromFunction(KafkaConsumerServiceLive(_))
