package com.immediatus.webApp
package web.service

import akka.actor.{ ActorSystem, ActorLogging, ActorRef, Props }
import akka.io.IO
import scala.concurrent.duration.{ Duration, DurationInt, FiniteDuration, MILLISECONDS }
import spray.can.Http
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import spray.routing.{ HttpServiceActor, Route }
import spray.routing.authentication.BasicAuth

object Service {

  case class Message(username: String, text: String)

  object Message extends DefaultJsonProtocol {
    implicit val format = jsonFormat2(apply)
  }

  def actor(settings : Settings)(implicit system : ActorSystem): ActorRef =
    system.actorOf(Props(new ServiceActor(settings)), "service")
}

class ServiceActor(settings : Settings) extends HttpServiceActor with ActorLogging with SprayJsonSupport {

  import context.dispatcher

  IO(Http)(context.system) ! Http.Bind(self, settings.host, settings.port)

  override def receive: Receive = runRoute(apiRoute ~ staticRoute)

  private val timeout = Duration(settings.timeout, MILLISECONDS)

  private def apiRoute =
    authenticate(BasicAuth(UserAuthenticator, "Chat")) { user =>
      pathPrefix("api") {
        path("messages") {
          get {
            produce(instanceOf[Seq[Service.Message]]) { completer => _ =>
              userChat(user.username) ! completer
            }
          } ~
          post {
            entity(as[Service.Message]) { message =>
              complete {
                userMessage(message.copy(username = user.username))
                StatusCodes.NoContent
              }
            }
          }
        } ~
        path("shutdown") {
          get {
            complete {
              val system = context.system
              system.scheduler.scheduleOnce(1 second)(system.shutdown())
              "Shutting down in 1 second ..."
            }
          }
        }
      }
    }

  private def staticRoute =
    path("") {
      getFromResource("web/index.html")
    } ~
    getFromResourceDirectory("web")

  private def userMessage(message : Service.Message) =
    context.children foreach (_ ! message)

  private def userChat(login : String) =
    context.child(login) getOrElse Chat.actor(timeout, login)
}
