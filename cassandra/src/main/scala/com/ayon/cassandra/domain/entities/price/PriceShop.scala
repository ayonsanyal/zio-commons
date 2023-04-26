package com.ayon.cassandra.domain.entities.price

import java.time.Instant

case class PriceShop(
  shop: Int,
  chnl: Int,
  scs: Long,
  wh: Int,
  gaid: Long,
  ct: Instant,
  ranking: String
)
