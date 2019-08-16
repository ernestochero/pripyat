package model.integration.nem

object NemRequests{
  trait NemRequest extends Product with Serializable
  case class NemHeartBeatRequest() extends NemRequest
}

object NemResponses{
  trait NemResponse extends Product with Serializable
  case class NemHeartBeatResponse(code: Int, `type`: Int, message: String) extends NemResponse
}

object NemExceptions{
  abstract class NemException(msg: String) extends Exception(msg)
  case class NemAPIException(msg: String) extends NemException(msg)
}