package co.ledger.be.http

import cats.effect.{IO, Sync}
import io.circe.{Decoder, Encoder, Printer}
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, EntityEncoder, HttpRoutes}
import org.http4s.circe.jsonEncoderWithPrinterOf
import org.http4s.circe.jsonOf

trait HttpController extends Http4sDsl[IO] {
  private val printer: Printer = Printer.noSpaces.copy(dropNullValues = true)

  implicit def circeEntityEncoder[F[_]: Sync, A: Encoder]: EntityEncoder[F, A] = jsonEncoderWithPrinterOf[F, A](printer)
  implicit def circeEntityDecoder[F[_]: Sync, A: Decoder]: EntityDecoder[F, A] = jsonOf[F, A]

  def routes: HttpRoutes[IO]
}
