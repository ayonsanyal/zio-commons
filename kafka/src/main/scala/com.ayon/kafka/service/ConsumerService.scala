package com.ayon.kafka.service

import org.apache.kafka.common.TopicPartition
import zio.{Chunk, IO, ZIO, Task}
import zio.json.{JsonDecoder, JsonEncoder}
import zio.kafka.consumer.{CommittableRecord, Consumer, Offset}
import zio.stream.ZStream

type ZIOKafkaStream[T]    = ZStream[Any, Throwable, CommittableRecord[String, T]]
type ZIOKafkaStreamOffset = ZStream[Any, Throwable, Offset]

trait ConsumerService:
  def consume[T <: AnyRef](topic: List[String])(implicit
    decoder: JsonDecoder[T],
    encoder: JsonEncoder[T]
  ): ZIO[Any, Throwable, ZIOKafkaStream[T]]

  def stop: Task[Unit]

  def commitOffsetBatches(topic: String, messageStream: ZIOKafkaStreamOffset): ZIO[Any, Throwable, Finished.type]

object ConsumerService:
  def consume[T <: AnyRef](topic: List[String])(implicit decoder: JsonDecoder[T], encoder: JsonEncoder[T]) =
    ZIO.serviceWithZIO[ConsumerService](_.consume[T](topic)(decoder, encoder))

  def stop = ZIO.serviceWithZIO[ConsumerService](_.stop)

  def commitOffsetBatches(topic: String, messageStream: ZIOKafkaStreamOffset) =
    ZIO.serviceWithZIO[ConsumerService](_.commitOffsetBatches(topic, messageStream))
