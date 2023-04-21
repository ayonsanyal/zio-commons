package com.channelpilot.di.kafka

import com.ayon.config.CPConfigLive
import com.ayon.kafka.config.{KafkaConfig, KafkaConfigLive}
import zio.kafka.consumer.{Consumer, Subscription}
import zio.kafka.producer.Producer
import zio.{ZIO, ZLayer}

object KafkaDependencies:
  // Adds all required dependencies for the producer service to the environment
  val configLayers = ConfigLive.live >+> KafkaConfigLive.live

  def autoProducerLayer =
    configLayers >+>
      ZLayer.scoped(KafkaConfig.getProducerSettings.flatMap(Producer.make)) >+>
      KafkaProducerServiceLive.live

  // Adds all required dependencies for the consumer service to the environment
  def autoConsumerLayer(groupId: String) =
    configLayers >+>
      ZLayer.scoped(autoConsumerEffect(groupId)) >+>
      KafkaConsumerServiceLive.live

  def autoConsumerEffect(groupId: String) = KafkaConfig.getConsumerSettings(groupId).flatMap(Consumer.make(_))
