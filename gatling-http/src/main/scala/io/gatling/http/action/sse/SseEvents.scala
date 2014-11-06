package io.gatling.http.action.sse

import akka.actor.ActorRef
import com.ning.http.client.{HttpResponseBodyPart, ListenableFuture}
import io.gatling.core.session.Session
import io.gatling.http.ahc.SseTx

/**
 * @author ctranxuan
 */
sealed trait SseEvent
case class OnSend(tx: SseTx) extends SseEvent
case class OnFailedOpen(tx: SseTx, errorMessage: String, time: Long) extends SseEvent
case class OnMessage(message: HttpResponseBodyPart, time: Long, sseHandler: SseHandler) extends SseEvent
case class OnThrowable(tx: SseTx, errorMessage: String, time: Long) extends SseEvent // FIXME consider using HttpEvent.OnThrowable instead
case class CheckTimeout(check: SseCheck) extends SseEvent

sealed trait SseUserAction extends SseEvent {
  def requestName: String
  def next: ActorRef
  def session: Session
}

case class SetCheck(requestName: String, check: SseCheck, next: ActorRef, session: Session) extends SseUserAction
case class CancelCheck(requestName: String, next: ActorRef, session: Session) extends SseUserAction
case class Close(requestName: String, next: ActorRef, session: Session) extends SseUserAction
case class Reconciliate(requestName: String, next: ActorRef, session: Session) extends SseUserAction
case class OnFuture(future: ListenableFuture[Unit]) extends SseEvent
