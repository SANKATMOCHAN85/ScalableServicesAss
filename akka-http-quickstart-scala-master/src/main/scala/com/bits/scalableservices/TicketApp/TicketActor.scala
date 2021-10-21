package com.bits.scalableservices.TicketApp

import akka.actor.{Actor, PoisonPill, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import com.bits.scalableservices.TicketApp.DBActor.{DBCommit, DBFetch}
import com.bits.scalableservices.TicketApp.HttpActor.getCurrentUser
import com.bits.scalableservices.TicketApp.TicketRoute.mapper
import com.bits.scalableservices.dao.{TicketDetails, User}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.util.Random
import scala.util.Random.nextInt

object TicketActor {
  case class bookTicket(body: TicketDetails)
  case class getTicket(userId: Int)
}

class TicketActor extends Actor {

  import TicketActor._

  override def receive: Receive = {
    case bookTicket(body: TicketDetails) => {
      //Get the user details from the other microservice
      //val httpActor = context.actorOf(Props[HttpActor], "HttpActor")
      //implicit val timeOut = Timeout(20 seconds)
      //val responseHttp = httpActor ? getCurrentUser()
      //val response = FutureHandler.futureWithTimeout[String](responseHttp, 10 seconds, "Failed", "Failed", "HttpActor")
      //val op = for (element <- response) yield {
        //httpActor ! PoisonPill
        //if (element.equalsIgnoreCase("Failed"))
         // "Not Booked"
        //else {
          //Store in the DB
        //  val user = mapper.readValue(element, classOf[User])
          val ticketNumberSelf = f"${('A' to 'Z') (nextInt(26))}${nextInt(10000)}%04d"
          println("ticketNumberSelf : "+ticketNumberSelf)
          val dbActor = context.actorOf(Props[DBActor], "DBActor")
          dbActor ! DBCommit(body,ticketNumberSelf)
          "Booked"
          dbActor ! PoisonPill
        //}
      //}
      sender ! ticketNumberSelf
    }
    case getTicket(userId: Int) => {
      val dbActor = context.actorOf(Props[DBActor], "DBActor")
      implicit val timeOut = Timeout(20 seconds)
      val ticketFromDB = dbActor ? DBFetch(userId)
      val response = FutureHandler.futureWithTimeout[List[TicketDetails]](ticketFromDB, 10 seconds, List(TicketDetails("","",null,null,"","","",0)), List(TicketDetails("","",null,null,"","","",0)), "DBActor")
      val op = for (element <- response) yield {
        element
      }
      op.pipeTo(sender)
    }
  }
}
