package model.commons.util

import akka.actor.ActorSystem
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future}

trait Request {

  def persistRequest[M](listBaseUri: List[String],
                        uri: String,
                        params: Map[String, String] = Map.empty[String, String],
                        headers: immutable.Seq[HttpHeader] = Nil,
                        entity: HttpEntity.Strict = HttpEntity.Empty,
                        method: HttpMethod = HttpMethods.GET
                       )(implicit um:Unmarshaller[String, M],  system: ActorSystem): Future[M] = {
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val ec: ExecutionContext = system.dispatcher

    def persisting(url: String, listUrl: List[String]): Future[M] ={
      val request = HttpRequest(
        uri = buildUrl(url, params).toString(),
        method = method,
        headers = headers,
        entity = entity
      )
      makeRequest[M](request) recoverWith {
        case ex: Exception =>
          if(listUrl.isEmpty) {
            println("All urlRetries have been failed")
            Future.failed[M](ex)
          }
          else {
            println("statusRetry-> currentUrl: " + url + "  nextUrlToUse: "+ listUrl.head + " queueUrlToRetry: "+ listUrl.tail)
            println("Retry with url: "+ listUrl.head)
            persisting(listUrl.head, listUrl.tail)
          }
      }
    }
    val urlsToRetry = listBaseUri.map(baseUri => baseUri + uri)
    persisting(urlsToRetry.head, urlsToRetry.tail)
  }

  def makeSingleRequest[M](baseUrl: String,
                           uri: String,
                           params: Map[String, String] = Map.empty[String, String],
                           headers: immutable.Seq[HttpHeader] = Nil,
                           entity: HttpEntity.Strict = HttpEntity.Empty,
                           method: HttpMethod = HttpMethods.GET
                          )(implicit um:Unmarshaller[String, M], system: ActorSystem): Future[M] = {
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val request = HttpRequest(
      uri = buildUrl(baseUrl + uri, params).toString(),
      method = method,
      headers = headers,
      entity = entity
    )
    makeRequest[M](request)
  }

  def makeRequest[M](request: HttpRequest)(implicit um:Unmarshaller[String, M], system: ActorSystem, materializer: ActorMaterializer): Future[M] = {
    implicit val ec: ExecutionContext = system.dispatcher

    def processResponse(httpResponse: HttpResponse): Future[M] = {
      httpResponse match {
        case HttpResponse(StatusCodes.OK, _, entity, _) =>
          println("Request Successfuly" )
          entity.dataBytes.runReduce(_++_).flatMap(bytes => Unmarshal(bytes.utf8String).to[M])
        case resp @ HttpResponse(code, _, _, _) =>
          println("Request failed, response code: " + code)
          resp.discardEntityBytes()
          Future.failed[M](new Exception("Request failed, response code: " + code))
      }
    }
    Http().singleRequest(request)
      .flatMap(processResponse)
  }

  def buildUrl(url:String, params: Map[String, String]): Uri = {
    Uri(url).withQuery(buildQuery(params))
  }

  def buildQuery(params: Map[String, String]): Query = {
    Query(params)
  }
}
