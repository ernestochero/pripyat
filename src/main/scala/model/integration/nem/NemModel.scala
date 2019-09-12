package model.integration.nem

object NemRequests{
  trait NemRequest extends Product with Serializable
  case class NemHeartBeatRequest() extends NemRequest
  case class NemStatusRequest() extends NemRequest
  case class NemGenerateAccountRequest() extends NemRequest

}

object NemResponses{
  trait NemResponse extends Product with Serializable
  case class NemHeartBeatResponse(code: Int, `type`: Int, message: String) extends NemResponse
  case class NemStatusResponse(code: Int, `type`: Int, message: String) extends NemResponse
  case class NemGenerateAccountResponse(privateKey: String, address: String, publicKey: String) extends NemResponse
}

abstract class APIIntegrationException(msg: String) extends Exception(msg)
case class GeneralAPIException(msg :String) extends APIIntegrationException(msg)

object NemExceptions{
  case class NemAPIException(msg :String) extends APIIntegrationException(msg)
}
