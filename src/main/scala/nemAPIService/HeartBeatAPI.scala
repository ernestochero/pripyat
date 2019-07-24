package nemAPIService

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import nemAPIService.models.HeartBeat
import request.Request
import spray.json.DefaultJsonProtocol



trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol{
  implicit val HeartBeatFormat = jsonFormat3(HeartBeat)
}

object HeartBeatAPI extends JsonSupport {
  def getHeartBeat() = {
    val request = new HeartBeatAPIRequest()
    request.makeRequest(Map.empty[String, String])
  }
}

case class HeartBeatAPIRequest() extends Request[HeartBeat]{

  override def uri(): String = "/heartbeat"
  override def baseUri: String = "http://hugetestalice.nem.ninja:7890"

}
