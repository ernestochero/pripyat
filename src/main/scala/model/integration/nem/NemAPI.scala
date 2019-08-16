package model.integration.nem

import java.util.concurrent.TimeUnit

import akka.actor._
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import model.commons.util.Request
import model.config.AppConfig._
import model.integration.nem.NemResponses.NemHeartBeatResponse
import model.integration.nem.NemExceptions.NemAPIException
import model.integration.nem.NemRequests.NemHeartBeatRequest
import scala.concurrent.Future
import scala.concurrent.duration._

case class NemAPI() {
  implicit val timeout: Timeout = Timeout(Duration.create(60, TimeUnit.SECONDS))

  val system = ActorSystem("Nem-System")
  implicit val ec = system.dispatcher

  val nemSync = system.actorOf(Props[NemSync], "nemSync")

  def getNemHeartBeat(): Future[NemHeartBeatResponse] ={
    val result = (nemSync ? NemHeartBeatRequest).mapTo[NemHeartBeatResponse]
    recoverWithNemAPIError(result)
  }

  def recoverWithNemAPIError[M](future: Future[M]): Future[M] = {
    future.recoverWith{
      case ex: Exception =>
        Future.failed[M](NemAPIException(ex.getMessage))
    }
  }

  def terminate(): Unit ={
    system.terminate()
  }
}


class NemSync extends Actor with Request with NemCodecsFormat{
  import context.dispatcher
  implicit val system = context.system

  val listBaseUri: List[String] = hostNem()

  def receive = {
    case NemHeartBeatRequest =>
      val currentSender = sender()
      val uri = NemAPIResources.nemHeartBeat()
      persistRequest[NemHeartBeatResponse](listBaseUri, uri) pipeTo currentSender
    case _ =>
      println("coming soon")
  }
}