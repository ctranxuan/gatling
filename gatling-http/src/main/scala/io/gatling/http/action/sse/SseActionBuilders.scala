package io.gatling.http.action.sse

import akka.actor.ActorDSL._
import akka.actor.ActorRef
import io.gatling.core.config.Protocols
import io.gatling.core.session.Expression
import io.gatling.http.action.HttpActionBuilder
import io.gatling.http.check.ws.WsCheck
import io.gatling.http.request.builder.sse.{SseCheck, SseOpenAction, SseOpenRequestBuilder}

/**
 * @author ctranxuan
 */
class SseOpenActionBuilder(
    requestName: Expression[String],
    sseName: String,
    requestBuilder: SseOpenRequestBuilder,
    check: Option[SseCheck] = None) extends HttpActionBuilder {

  def check(check: SseCheck) = new SseOpenActionBuilder(requestName, sseName, requestBuilder, Some(check))

  def check(wsCheck: WsCheck): SseOpenActionBuilder = check(SseCheck(wsCheck, wsCheck.blocking, wsCheck.timeout, wsCheck.expectation))

  override def build(next: ActorRef, protocols: Protocols): ActorRef = {
    val request = requestBuilder.build(httpProtocol(protocols))
    val protocol = httpProtocol(protocols)

    actor(new SseOpenAction(requestName, sseName, request, check, next, protocol))
  }
}

class SseCloseActionBuilder(requestName: Expression[String], sseName: String) extends HttpActionBuilder {

  override def build(next: ActorRef, protocols: Protocols): ActorRef = actor(new SseCloseAction(requestName, sseName, next))
}