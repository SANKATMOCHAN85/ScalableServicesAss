// You may not use this file except in compliance with the prior approval
package com.bits.scalableservices.db

import com.bits.scalableservices.dao.TicketDetails

import java.sql.Connection
import java.sql.Statement
import scala.collection.mutable.ListBuffer

case class ConnectionParameters(dBUrl: String, dbUser: Option[String] = None, dbPassword: Option[String] = None,
                                params: Option[Map[String, String]] = None)

case class Connections(conn: Connection, st: Statement)

trait Record

trait DBConnection {

  def createConnection()
  def closeConnection(): Unit
  def fetchRecords(table: String, query: String): ListBuffer[TicketDetails]
  def insertRecords(table: String, query: String)

}

class FetchMetaData {
  self: DBConnection ⇒
  def createConn(): Unit = {
    self.createConnection()
  }
  def closeConn(): Unit = {
    self.closeConnection()
  }
  def fetchMultipleRecords(table: String, query: String): ListBuffer[TicketDetails] = {
    self.fetchRecords(table, query)
  }
  def insertTicketRecords(table: String, query: String) = {
    self.insertRecords(table, query)
  }
}

