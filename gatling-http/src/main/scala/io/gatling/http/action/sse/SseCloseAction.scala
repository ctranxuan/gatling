package io.gatling.http.action.sse

import akka.actor.ActorRef
import io.gatling.core.session.{ Expression, Session }
import io.gatling.core.validation.Validation
import io.gatling.http.action.RequestAction
import io.gatling.http.request.builder.sse.Close

/**
 * @author ctranxuan
 */
class SseCloseAction(val requestName: Expression[String], sseName: String, val next: ActorRef) extends RequestAction with SseAction {

  def sendRequest(requestName: String, session: Session): Validation[Unit] = {
    logger.error("SseCloseAction.sendRequest: ****************-" + session.userId)
    for {
      sseActor <- fetchSse(sseName, session)
    } yield sseActor ! Close(requestName, next, session)

  }
}
