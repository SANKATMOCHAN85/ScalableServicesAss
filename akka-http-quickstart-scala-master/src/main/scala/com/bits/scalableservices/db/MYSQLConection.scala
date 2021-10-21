// You may not use this file except in compliance with the prior approval
package com.bits.scalableservices.db

import com.bits.scalableservices.TicketApp.TicketRoute.mapper
import com.bits.scalableservices.dao.TicketDetails

import java.sql.DriverManager
import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeFormatter
import scala.collection.mutable.ListBuffer
import scala.util.Try
import scala.util.parsing.json.JSON.headOptionTailToFunList

trait MYSQLConnection extends DBConnection {

  private[MYSQLConnection] var con: Connections = _

  override def createConnection(): Unit = {
    Class.forName("com.mysql.cj.jdbc.Driver")
    val connectionTry = Try(DriverManager.getConnection("jdbc:mysql://sql6.freesqldatabase.com:3306?serverTimezone=UTC&useSSL=false","sql6445125", "l9KTz4ueJr"))
    connectionTry.isSuccess match {
      case true =>
        val connections = connectionTry.get
        val statements = connections.createStatement
        con = Connections(connections, statements)
      case false => {
        println("Connection not made")
        println("exception stacktrace ="+ connectionTry.failed.get.getStackTrace.mkString(","))
      }
    }
  }

  override def closeConnection(): Unit = {
    con.st.close()
    con.conn.close()
  }


  override def insertRecords(table: String, query: String) = {
    println("New Query :"+query)
    val resultSet =  Try(con.st.executeUpdate(query))
    resultSet.isSuccess match {
      case true => {
        resultSet.get
        println("Records Inserted in table "+table)
      }
      case false => {
        println("Error occured "+resultSet.failed.get.getMessage)
      }
    }
  }

  override def fetchRecords(table: String, query: String): ListBuffer[TicketDetails] = {
    val stmt = con.st
    val resultSetTry = Try(stmt.executeQuery(query))
    println("query: "+query)
    val ticketDetailsSent = ListBuffer[TicketDetails]()
    resultSetTry.isSuccess match {
      case true =>
        {
          val rs = resultSetTry.get
          while(rs.next())
            {
              val ticketDetails = TicketDetails(
              converter(rs.getString("TravellingFrom")),
                converter(rs.getString("TravellingTo")),
                converter(rs.getString("DateOfBooking")),
                converter(rs.getString("DateOfTravel")),
                converter(rs.getString("PassangerName")),
                converter(rs.getString("VehicleName")),
                converter(rs.getString("TicketNumber")),
                converter(rs.getString("UserId")).toInt)
              ticketDetailsSent.+=: (ticketDetails)
            }
            rs.close()
          ticketDetailsSent
        }
      case false => {
        println("Invalid Query, not selected anything")
        null
      }
    }
  }

  def converter(s: String): String = {
    if (s == null) null //scalastyle:ignore
    else if (s.equals("")) null else s //scalastyle:ignore
  }
}
