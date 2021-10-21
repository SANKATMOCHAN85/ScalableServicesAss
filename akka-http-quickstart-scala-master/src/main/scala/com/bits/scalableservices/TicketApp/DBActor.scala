package com.bits.scalableservices.TicketApp

import akka.actor.Actor
import com.bits.scalableservices.dao.TicketDetails
import com.bits.scalableservices.TicketApp.DBActor.{DBCommit, DBFetch}
import com.bits.scalableservices.db.{FetchMetaData, MYSQLConnection}

object DBActor{
  case class DBCommit(ticketDetails: TicketDetails, ticketNumber: String)
  case class DBFetch(userId: Int)
}

class DBActor extends Actor{
  val mySQLConnection = new FetchMetaData with MYSQLConnection

  override def receive: Receive = {
    case DBCommit(ticketDetails: TicketDetails, ticketNumber: String) => {
          val query = s"""insert into sql6445125.Ticket(`TravellingFrom`,`TravellingTo`,`DateOfBooking`,`DateOfTravel`,`PassangerName`,`VehicleName`,`TicketNumber`,`UserId`) values ("${ticketDetails.From}","${ticketDetails.destination}","${ticketDetails.dateOfBooking}","${ticketDetails.dateOfTravel}","${ticketDetails.passengers}","${ticketDetails.vehicleName}","${ticketNumber}","${ticketDetails.userId}")""".stripMargin
          mySQLConnection.createConnection()
          mySQLConnection.insertRecords("Ticket", query)
          mySQLConnection.closeConnection()
    }
    case DBFetch(userId: Int)  => {
      val query = s"""SELECT * FROM sql6445125.Ticket where UserId = '${userId}'"""
      mySQLConnection.createConnection()
      val ticketDetails = mySQLConnection.fetchRecords("Ticket", query).toList
      mySQLConnection.closeConnection()
      sender ! ticketDetails
    }
  }
}
