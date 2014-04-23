package com.immediatus.webApp
package web

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object Boot extends App {


  implicit val system = ActorSystem("chat")
  val config = ConfigFactory.load()

  val database = domain.DB.mongoDB(domain.db.Settings(config))

  val persistentActor = domain.Persistent.actor(database)
  val authActor       = service.UserAuthenticator.actor(persistentActor)
  val serviceActor    = service.Service.actor(service.Settings(config))

  system.awaitTermination()
}
