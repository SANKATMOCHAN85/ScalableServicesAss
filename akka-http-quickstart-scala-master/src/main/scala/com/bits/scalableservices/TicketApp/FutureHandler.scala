package com.bits.scalableservices.TicketApp

import java.util.{Timer, TimerTask}
import java.util.concurrent.TimeUnit
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.concurrent.duration.FiniteDuration

object FutureHandler {
    implicit final def timer[A](codeBlock: ⇒ A): (A, Long) = {
      val startTime = System.nanoTime()
      val result = codeBlock
      val endTime = System.nanoTime()
      val diffTime = TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS)
      (result, diffTime)
    }



    def futureWithTimeout[T](future: Future[Any], timeout: FiniteDuration, resOnTimeout: T, resOnException: T,
                             profile: String)(implicit ec: ExecutionContext): Future[T] = {
      val timer: Timer = new Timer(true)
      val p = Promise[T]
      val timerTask = new TimerTask() { // and a Timer task to handle timing out
        def run(): Unit = {
          //Logg.logInError(uuid, s"Timeout Occurred for Actor : ${profile}", tid)
          p.trySuccess(resOnTimeout.asInstanceOf[T])
        }
      }
      timer.schedule(timerTask, timeout.toMillis) // Set the timeout to check in the future
      future.map {
        result =>
          if (p.trySuccess(result.asInstanceOf[T]))
            timer.cancel()
      }
        .recover {
          case e: Exception => {
            //Logg.logInError(uuid, s"Exception Occurred for Actor ${profile} : " + e.getStackTrace.mkString(","), tid)
            timer.cancel()
            p.trySuccess(resOnException.asInstanceOf[T])
          }
        }
      p.future
    }
}
