package com.bits.scalableservices.TicketApp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

object TicketMain extends App
{
  //val config = ConfigFactory.load("app.conf")
  implicit val aSystem = ActorSystem("TICKETBOOKING")
  implicit val materializer = ActorMaterializer()
  //actorSystem = system
  val routes: Route = TicketRoute.routes
  Http().bindAndHandle(routes, "127.0.0.1", 8082)
}
