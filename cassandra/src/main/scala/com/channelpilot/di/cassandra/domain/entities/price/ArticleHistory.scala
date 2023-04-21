package com.channelpilot.di.cassandra.domain.entities.price

case class ArticleHistory(
  shopId: Int,
  groupId: Int,
  channelId: Int,
  updateId: Int,
  shardCount: Int,
  shardId: Int,
  gaid: Int,
  data: String
)
