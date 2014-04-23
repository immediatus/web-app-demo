package com.immediatus.webApp
package web.service

import scala.concurrent.{ Future, Promise }

import akka.actor.{ Actor, ActorRef, ActorSystem, ActorLogging, Props }
import spray.routing.authentication.{ BasicUserContext, UserPass, UserPassAuthenticator }


object UserAuthenticator extends UserPassAuthenticator[BasicUserContext] {

  override def apply(userPass: Option[UserPass]): Future[Option[BasicUserContext]] = {
    val basicUserContext =
      userPass flatMap {
        case UserPass(username, password) if username == password => Some(BasicUserContext(username))
        case _ => None
      }
    Promise.successful(basicUserContext).future
  }

  case class ValidateUser(login : String, password : String)
  case object InvalidUser
  case class ValidUser(name : String)

  def actor(repo : ActorRef)(implicit system : ActorSystem): ActorRef =
    system.actorOf(Props(new UserAuthenticatorActor(repo)), "auth")
}

class UserAuthenticatorActor(persistent : ActorRef) extends Actor with ActorLogging {
  import domain.Persistent

  def receive : Actor.Receive = {
    case UserAuthenticator.ValidateUser(login, passwd) =>
      persistent ! Persistent.GetUserByLogin(login, passwd)
  }

}
