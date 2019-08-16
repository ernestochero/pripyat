package app

import model.integration.nem.NemAPI
import model.integration.nem.NemExceptions.NemAPIException
import model.integration.nem.NemResponses.NemHeartBeatResponse

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object App {
  def main(args: Array[String]): Unit = {
    println(s"Start testing NEM API")
    val nemAPI = NemAPI()

    // Test NemHeartBeat
    val futureResultNemHeartBeat = nemAPI.getNemHeartBeat().recover{
      case NemAPIException(msg) =>{
        println("FailureOnNemAPI HeartBeat: "+msg)
        NemHeartBeatResponse(0, 1, "TEST recover 1")
      }
    }
    val futureResponseNemHeartBeat = Await.result(futureResultNemHeartBeat, 60.seconds)
    println("resultNemHeartBeat: "+ futureResponseNemHeartBeat.toString)

    // Test X
    //...
    //...

    nemAPI.terminate()
  }
}

