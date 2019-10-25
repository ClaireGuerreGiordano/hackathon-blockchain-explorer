package co.ledger.be.service

import cats.effect.IO
import co.ledger.be.Model.{Block, Transaction}
import io.circe.JsonObject

class BEService(provider: Provider) {
  def getTransaction(hash: String): IO[JsonObject] = provider.getTransaction(hash)

  def getBalance(address: String): IO[BigDecimal] = provider.getBalance(address)

  def getTransactions(xpub: String): IO[List[Transaction]] = provider.getTransactions(xpub)

  def getBlock(height: String): IO[Block] = provider.getBlock(height)
}
