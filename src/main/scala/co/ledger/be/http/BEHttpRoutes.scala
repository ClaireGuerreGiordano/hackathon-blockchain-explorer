package co.ledger.be.http


import cats.effect.IO
import co.ledger.be.service.BEService
import com.typesafe.scalalogging.StrictLogging
import org.http4s.HttpRoutes

class BEHttpRoutes(beService: BEService) extends HttpController with StrictLogging {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {

    case GET -> Root / "blockchain" / "v3" / "blocks" / blockHeight =>
      for {
        block <- beService.getBlock(blockHeight)
        status <- Ok(block)
      } yield status

    case GET -> Root / "blockchain" / "v3" / "address" / address / "transactions" =>
      for {
        tx <- beService.getTransactions(address)
        status <- Ok(tx)
      } yield status

    case GET -> Root / "blockchain" / "v3" / "address" / address / "balance" =>
      for {
        balance <- beService.getBalance(address)
        status <- Ok(balance)
      } yield status

    case GET -> Root / "blockchain" / "v3" / "transaction" / hash =>
      for {
        tx <- beService.getTransaction(hash)
        status <- Ok(tx)
      } yield status

  }
}
