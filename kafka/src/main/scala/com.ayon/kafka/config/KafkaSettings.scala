package com.ayon.kafka.config

import zio.*
import zio.kafka.consumer.*
import zio.kafka.consumer.Consumer.{AutoOffsetStrategy, OffsetRetrieval}
import zio.kafka.producer.ProducerSettings

final case class KafkaSettings(kafka: Kafka):
  def consumerSettings(groupId: String) = ConsumerSettings(kafka.hosts)
    .withGroupId(groupId)
    .withCloseTimeout(kafka.closeTimeOut.seconds)
    .withOffsetRetrieval(OffsetRetrieval.Auto(AutoOffsetStrategy.Earliest))

  def producerSettings = ProducerSettings(kafka.hosts)
