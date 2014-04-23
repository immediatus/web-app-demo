package com.immediatus.webApp
package web.service

import akka.actor.{ Actor, ActorRef, ActorContext, FSM, Props }
import scala.concurrent.duration.FiniteDuration

object Chat {

  type Completer = Seq[Service.Message] => Unit

  sealed trait State

  object State {
    case object Waiting extends State
    case object WaitingForMessage extends State
    case object WaitingForCompleter extends State
  }

  case object Timeout

  def actor(timeoutDuration: FiniteDuration, name : String)(implicit system : ActorContext): ActorRef =
    system.actorOf(Props(new ChatActor(timeoutDuration)), name)
}

class ChatActor(timeoutDuration: FiniteDuration) extends Actor with FSM[Chat.State, (Option[Chat.Completer], Seq[Service.Message])] {
  import Chat._

  startWith(State.Waiting, (None, Nil))

  when(State.Waiting) {
    case Event(completer: Chat.Completer, (None, Nil))  =>
      goto(State.WaitingForMessage) using Some(completer) -> Nil

    case Event(message: Service.Message, (None, Nil))   =>
      goto(State.WaitingForCompleter) using None -> (message +: Nil)

    case Event(Timeout, _)                              =>
      stop()
  }

  when(State.WaitingForMessage) {
    case Event(completer: Chat.Completer, (_, Nil)) =>
      stay using Some(completer) -> Nil

    case Event(message: Service.Message, (Some(completer), Nil)) =>
      completer(message +: Nil)
      goto(State.Waiting) using None -> Nil

    case Event(Timeout, (Some(completer), Nil)) =>
      completer(Nil)
      goto(State.Waiting) using None -> Nil
  }

  when(State.WaitingForCompleter) {
    case Event(completer: Chat.Completer, (None, messages)) =>
      completer(messages)
      goto(State.Waiting) using None -> Nil

    case Event(message: Service.Message, (None, messages)) =>
      stay using None -> (message +: messages)

    case Event(Timeout, _) =>
      stop()
  }

  onTransition {
    case _ -> State.WaitingForMessage | _ -> State.Waiting => setTimeout()
  }

  def setTimeout(): Unit =
    setTimer("timeout", Timeout, timeoutDuration, false)
}
