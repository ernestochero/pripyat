import nemAPIService.HeartBeatAPI

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object Main {
  def main(args: Array[String]): Unit = {
    println(s"test Init")
    val result = HeartBeatAPI.getHeartBeat()
    result.onComplete {
      case Success(heartBeat) =>
        println(s"value : ${heartBeat.toString}")
      case Failure(exception) =>
        println(s"fail : ${exception.getMessage}")
    }
  }
}
