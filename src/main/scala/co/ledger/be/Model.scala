package co.ledger.be

import io.circe.{Decoder, Encoder, Json}
import io.circe.generic.semiauto.deriveDecoder
import io.circe.generic.semiauto.deriveEncoder
object Model {

  case class Block(blockHash: String)

  object Block {
    implicit def decoder: Decoder[Block] = deriveDecoder
    implicit def encoder: Encoder[Block] = deriveEncoder

  }

  case class ScriptPubKey(hex: String, addresses: List[String])

  object ScriptPubKey {
    implicit def decoderList: Decoder[List[String]] = deriveDecoder
    implicit def decoder: Decoder[ScriptPubKey] = deriveDecoder
    implicit def encoder: Encoder[ScriptPubKey] = deriveEncoder
  }

  case class ScriptSig(hex: String)

  object ScriptSig {
    implicit def decoder: Decoder[ScriptSig] = deriveDecoder
    implicit def encoder: Encoder[ScriptSig] = deriveEncoder
  }

  case class Vin(txid: String, vout: BigInt, n: BigInt, scriptSig: ScriptSig, addresses: String, value: String)

  object Vin {
    implicit val encodeString: Encoder[String] = {
      case null => Json.fromString("")
      case s => Json.fromString(s)
    }
    implicit def decoder: Decoder[Vin] = deriveDecoder
    implicit def encoder: Encoder[Vin] = deriveEncoder
  }

  case class Vout(value: String, n: BigInt, scriptPubKey: ScriptPubKey, spent: Boolean)

  object Vout {
    implicit def decoder: Decoder[Vout] = deriveDecoder
    implicit def encoder: Encoder[Vout] = deriveEncoder
  }

  case class Transaction(txid: String, version: BigInt, locktime: BigInt, vin: List[Vin], vout: List[Vout], blockhash: String, blockheight: BigInt,
                         confirmations: BigInt, blocktime: BigInt, valueOut: String, valueIn: String, fees: String, hex: String)


  object Transaction {
    implicit def decoder: Decoder[Transaction] = deriveDecoder
    implicit def encoder: Encoder[Transaction] = deriveEncoder
  }

  // Business errors
  sealed trait ApiError extends Product with Serializable
  case class OtherError(msg: String) extends ApiError

}
