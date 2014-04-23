package com.immediatus.webApp
package domain

import akka.actor.{ Actor, ActorRef, ActorSystem, ActorLogging, Props }

object Persistent {
  case class GetUserByLogin(login : String, passwd : String)
  case class UserByLogin(user : Option[User])

  case class GetMessages(time : Long)
  case class GetUserMessages(login : String, time : Long)
  case class Messages(msgs : Seq[Message])

  def actor(database : DB)(implicit system : ActorSystem): ActorRef =
    system.actorOf(Props(new PersistentActor(database)), "persistent")
}

class PersistentActor(database : DB) extends Actor with ActorLogging {
  private val users     = database.repo.users
  private val messages  = database.repo.messages

  def receive : Actor.Receive = userReceive orElse messagesReceive

  /**------------------------------------------------------------------------------------------------------------------
    * Actor Receive:
    * - Persistent.GetUserByLogin
    *------------------------------------------------------------------------------------------------------------------
    */
  def userReceive : Actor.Receive = {
    case Persistent.GetUserByLogin(login, passwd) =>
      sender ! Persistent.UserByLogin { users.getUserByLogin(login, passwd) }
  }

  /**------------------------------------------------------------------------------------------------------------------
    * Actor Receive:
    * - Persistent.GetMessages
    * - Persistent.GetUserMessages
    *------------------------------------------------------------------------------------------------------------------
    */
  def messagesReceive : Actor.Receive = {
    case Persistent.GetUserMessages(login, time) =>
      sender ! Persistent.Messages { messages.getUserMessages(login, time) }
  }
}
