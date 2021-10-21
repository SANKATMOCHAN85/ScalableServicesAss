package com.bits.scalableservices.TicketApp

import akka.actor.Props
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.HttpApp
import akka.pattern.ask
import akka.util.Timeout
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.bits.scalableservices.TicketApp.TicketActor.{bookTicket, getTicket}
import com.bits.scalableservices.TicketApp.TicketMain.aSystem
import com.bits.scalableservices.dao.TicketDetails
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

object TicketRoute {

  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)
  mapper.setSerializationInclusion(Include.NON_NULL)


  def routes: Route = cors(){
    (path("confirmation")) {
      post {
        entity(as[String]) { ticketDetails =>
          val inputBody = mapper.readValue(ticketDetails, classOf[TicketDetails])
          println("InputParsedBody "+inputBody)
          val actor = aSystem.actorOf(Props[TicketActor], "TicketActor")
          implicit val timeout = Timeout(50 seconds)
          val reponse = actor ? bookTicket(inputBody)
          val outPut = Await.result(reponse, timeout.duration)
          aSystem.stop(actor)
          complete("Your Ticket is booked, Ticket Number is: " + outPut)
        }
      }
    } ~
      ((path("getticket" / Segment) & get) & withRequestTimeout(50 seconds)) { (userId) =>
        val actor = aSystem.actorOf(Props[TicketActor], "TicketActor")
        implicit val timeout = Timeout(50 seconds)
        val response = actor ? getTicket(userId.toInt)
        val timeoutFuture = FutureHandler.futureWithTimeout[List[TicketDetails]](response, 10 seconds, List(TicketDetails("", "", null, null, "", "", "", 0)), List(TicketDetails("", "", null, null, "", "", "", 0)), "TicketActor")
        onSuccess(timeoutFuture) { result =>
          val resultMap = mapper.writeValueAsString(result)
          val sendingResponse = HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, resultMap))
          aSystem.stop(actor)
          complete(sendingResponse)
        }
      }
  }
}
