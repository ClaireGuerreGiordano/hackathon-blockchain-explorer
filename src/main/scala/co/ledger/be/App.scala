package co.ledger.be

import java.util.concurrent.Executors

import cats.effect.{ExitCode, IO, IOApp, Resource}
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.ExecutionContext

object App extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    val resources = for {
      config <- Resource.liftF(Config.load)
      client <- BlazeClientBuilder[IO](ExecutionContext.fromExecutorService(Executors.newCachedThreadPool()))
        .resource
    } yield (config, client)

    resources.use { case (config, client) =>
      Server.initialize(config, client).compile.lastOrError
    }
  }
}
