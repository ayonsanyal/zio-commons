package com.ayon.kafka.config

trait KafkaTopic:
  def prefix: String
  def topic: String
  def groupId: String