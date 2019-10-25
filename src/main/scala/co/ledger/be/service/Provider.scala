package co.ledger.be.service

import cats.effect.IO
import co.ledger.be.Model
import io.circe.JsonObject

trait Provider {
  def getTransaction(hash: String): IO[JsonObject]

  def getBalance(address: String): IO[BigDecimal]

  def getTransactions(address: String): IO[List[Model.Transaction]]

  def getBlock(height: String): IO[Model.Block]

}