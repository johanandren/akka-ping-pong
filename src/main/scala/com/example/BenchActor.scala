/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.example

import akka.actor.{Actor, ActorLogging, Props}

class BenchActor(count: Int, actorPairs: Int) extends Actor with ActorLogging{

  for { i <- 1 to actorPairs } yield {
    val ref = context.actorOf(Props(new PingActor(count)))
    ref ! PingActor.Initialize
    ref
  }

  var results = List.empty[PingActor.Result]

  def receive = {

    case res :PingActor.Result =>
      results = res :: results
      if (results.size == actorPairs) {
        val (totalCount, totalThroughput) = results.foldLeft((0, 0D)) { case ((totalMsgCount, totalMsgPerS), res) =>
          val timeS = res.totalTime / 1000000000D
          val msgCount = res.interactions * 2
          val msgsPerS = msgCount / timeS
          // log.info(f"${msgCount} messages, ${timeS} s, ${msgsPerS} msgs/s")
          (msgCount + totalMsgCount, totalMsgPerS + msgsPerS)
        }
        log.info(f"$actorPairs actor pairs exchanging a total of ${totalCount} messages, ${totalThroughput}%.0f msgs/s")

        context.system.terminate()
      }


  }

}
