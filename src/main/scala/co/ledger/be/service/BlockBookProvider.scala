package co.ledger.be.service
import cats.effect.IO
import co.ledger.be.Model
import co.ledger.be.Model.Block
import com.typesafe.scalalogging.StrictLogging
import io.circe.{Decoder, JsonObject}
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.{Header, Headers, Method, Request, Uri}
import org.http4s.client.Client
import org.http4s.circe.CirceEntityDecoder._


class BlockBookProvider(client: Client[IO]) extends Provider with StrictLogging {

  val root: Uri = Uri.fromString("https://ltc.blockbook.api.openbazaar.org/api").right.get

  def getRequest(uri: Uri): Request[IO] = {
    val req = Request[IO](Method.GET, uri, headers = Headers.of(Header("Accept", "application/json")))
    req
  }

  override def getTransactions(xpub: String): IO[List[Model.Transaction]] = client
    .expect[List[Model.Transaction]](getRequest(root / "xpub" / xpub))


  override def getBalance(address: String): IO[BigDecimal] = client
    .expect[ResponseAddress](getRequest(root / "address" / address)).map(x => BigDecimal(x.balance))


  override def getTransaction(txId: String): IO[JsonObject] = {
    client
    .expect[JsonObject](getRequest(root / "tx" / txId))}


  override def getBlock(height: String): IO[Model.Block] = client
    .expect[Block](getRequest(root / "block-index" / height))

  case class ResponseAddress(
    page: BigInt,
    totalPages: BigInt,
    itemsOnPage: BigInt,
    addrStr: String,
    balance: String,
    totalReceived: String,
    totalSent: String,
    unconfirmedBalance: String,
    unconfirmedTxApperances: BigInt,
    txApperances: BigInt,
    transactions: List[String]
  )

  object ResponseAddress {
    implicit def decoder: Decoder[ResponseAddress] = deriveDecoder
  }

}
