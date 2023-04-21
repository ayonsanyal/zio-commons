package com.ayon.kafka.serde

import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.{Deserializer, Serdes, Serializer}
import zio.{Task, ZIO, json}
import zio.json.{DecoderOps, EncoderOps, JsonDecoder, JsonEncoder}
import zio.kafka.serde
import zio.kafka.serde.Serde
import zio.kafka.serde
import zio.kafka.serde.Serde

object ZIOKafkaSerdeProvider:

  // Serde for zio-kafka implementation
  def kafkaSerde[T <: AnyRef](implicit decoder: JsonDecoder[T], encoder: JsonEncoder[T]): Serde[Any, T] = Serde.string.inmapM { asString =>
    ZIO.fromEither(asString.fromJson[T].left.map(new SerializationException(_)))
  } { asObject =>
    ZIO.attempt(asObject.toJson)
  }

  // Serializer for akka-kafka implementation
  def kafkaSerializer[T <: AnyRef](implicit encoder: JsonEncoder[T]): Serializer[T] = { (kafkaTopic: String, kafkaMessage: T) =>
    Serdes.String().serializer.serialize(kafkaTopic, kafkaMessage.toJson)
  }

  def kafkaDeSerializer[T <: AnyRef](implicit decoder: JsonDecoder[T]): Deserializer[T] =
    (kafkaTopic: String, kafkaSerializedMessage: Array[Byte]) =>
      Serdes.String().deserializer.deserialize(kafkaTopic, kafkaSerializedMessage).fromJson[T] match {
        case Left(value)  => throw new SerializationException(value)
        case Right(value) => value
      }

  def kafkaZIOKeySerializer: Task[serde.Serializer[Any, String]] =
    zio.kafka.serde.Serializer.fromKafkaSerializer(kafkaSerializer(JsonEncoder.string), Map.empty, isKey = true)

  def kafkaZIOValueSerializer[T <: AnyRef](implicit encoder: JsonEncoder[T]): Task[serde.Serializer[Any, T]] =
    zio.kafka.serde.Serializer.fromKafkaSerializer(kafkaSerializer(encoder), Map.empty, isKey = false)
