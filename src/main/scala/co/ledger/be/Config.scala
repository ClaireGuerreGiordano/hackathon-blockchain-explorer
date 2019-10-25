package co.ledger.be

import cats.effect.IO
import com.typesafe.config.ConfigFactory
import pureconfig.generic.auto._


case class Config(server: ServerConfig)

case class ProxyConfig(host: String, port: Int)

object Config {

  def load: IO[Config] = IO {
    pureconfig.loadConfigOrThrow[Config](ConfigFactory.load)
  }
}

case class ServerConfig(host: String, port: Int, path: String)




