package com.ayon.kafka.config

import zio.config.ReadError
import zio.kafka.consumer.ConsumerSettings
import zio.kafka.producer.ProducerSettings
import zio.{IO, ZIO, ZLayer}

trait KafkaConfig:
  def getKafkaSettings: IO[ReadError[String], KafkaSettings]
  def getConsumerSettings(groupId: String): IO[ReadError[String], ConsumerSettings]
  def getProducerSettings: IO[ReadError[String], ProducerSettings]

object KafkaConfig:
  def getKafkaSettings                     = ZIO.serviceWithZIO[KafkaConfig](_.getKafkaSettings)
  def getConsumerSettings(groupId: String) = ZIO.serviceWithZIO[KafkaConfig](_.getConsumerSettings(groupId))
  def getProducerSettings                  = ZIO.serviceWithZIO[KafkaConfig](_.getProducerSettings)
