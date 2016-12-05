package com.example

import akka.actor.{Actor, ActorLogging, Props}

class PingActor(total: Int) extends Actor with ActorLogging {

  import PingActor._

  var counter = 0
  val pongActor = context.actorOf(PongActor.props, "pongActor")
  var start = 0L
  def receive = {
    case Initialize =>
      start = System.nanoTime()
      log.info("In PingActor - starting ping-pong")
      pongActor ! PingMessage("ping")
    case PongActor.PongMessage(text) =>
      counter += 1
      if (counter == total) {
        context.parent ! Result(counter, System.nanoTime() - start)
        context.stop(self)
      } else {
        sender() ! PingMessage("ping")
      }
  }
}

object PingActor {
  case object Initialize
  case class Result(interactions: Int, totalTime: Long)

  case class PingMessage(text: String)

}