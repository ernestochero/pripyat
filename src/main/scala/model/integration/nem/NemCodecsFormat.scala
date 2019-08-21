package model.integration.nem

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import model.integration.nem.NemResponses._
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol

trait NemCodecsFormat extends JsonSupport{
  implicit val HeartBeatFormat: RootJsonFormat[NemHeartBeatResponse] = jsonFormat3(NemHeartBeatResponse)
  implicit val StatusFormat: RootJsonFormat[NemStatusResponse] = jsonFormat3(NemStatusResponse)
  implicit val GenerateAccountFormat: RootJsonFormat[NemGenerateAccountResponse] = jsonFormat3(NemGenerateAccountResponse)
}
