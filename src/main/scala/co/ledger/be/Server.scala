package co.ledger.be

import java.util.concurrent.Executors

import cats.effect.{ConcurrentEffect, ExitCode, IO, Timer}
import co.ledger.be.http.BEHttpRoutes
import co.ledger.be.service.{BEService, BlockBookProvider}
import fs2.Stream
import io.prometheus.client.CollectorRegistry
import org.http4s.Http
import org.http4s.client.Client
import org.http4s.metrics.prometheus.Prometheus
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Metrics
import org.http4s.syntax.kleisli._

import scala.concurrent.ExecutionContext

object Server {
  def initialize(config: Config, c: Client[IO])
                (implicit F: ConcurrentEffect[IO], timer: Timer[IO])
  : Stream[IO, ExitCode] = {

    lazy val collectorRegistry: CollectorRegistry = new CollectorRegistry

    lazy val provider = new BlockBookProvider(c)
    lazy val beService = new BEService(provider)
    lazy val beHttpRoutes = new BEHttpRoutes(beService)


    val controllers = List(
      beHttpRoutes
    )

    val router: IO[Http[IO, IO]] = Prometheus[IO](collectorRegistry, "http").map{
      mo => Metrics[IO](mo) {
        Router[IO](controllers.map(config.server.path -> _.routes): _*)
      }.orNotFound
    }

    for {
      router <- Stream.eval(router)
      exitCode <- BlazeServerBuilder[IO]
        .bindHttp(config.server.port, config.server.host)
        .withHttpApp(router)
        .withExecutionContext(ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(10)))
        .serve
    } yield exitCode
  }
}
