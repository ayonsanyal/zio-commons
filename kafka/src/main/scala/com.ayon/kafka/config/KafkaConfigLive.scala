package com.ayon.kafka.config

import com.ayon.commons.config.Config
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import zio.IO
import zio.Task
import zio.ZIO
import zio.ZLayer
import zio.config.ConfigSource
import zio.config.PropertyTreePath
import zio.config.ReadError
import zio.config.magnolia.descriptor
import zio.config.read
import zio.config.typesafe.TypesafeConfigSource
import zio.kafka.consumer.ConsumerSettings
import zio.kafka.producer.ProducerSettings

case class KafkaConfigLive(config: Config) extends KafkaConfig:
  override def getKafkaSettings                     = read(descriptor[KafkaSettings].from(Config.configSource))
  override def getConsumerSettings(groupId: String) = getKafkaSettings.map(_.consumerSettings(groupId))
  override def getProducerSettings                  = getKafkaSettings.map(_.producerSettings)

object KafkaConfigLive:
  val live = ZLayer.fromFunction(KafkaConfigLive(_))
