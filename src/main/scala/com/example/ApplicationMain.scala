package com.example

import akka.actor.{ActorSystem, Props}

object ApplicationMain extends App {
  val system = ActorSystem("MyActorSystem")
  system.actorOf(Props(new BenchActor(1000000000, 1)))
}