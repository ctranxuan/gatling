package io.gatling.http.request.builder.sse

import io.gatling.core.session.{ SessionPrivateAttributes, Expression }
import io.gatling.http.action.sse.SseCloseActionBuilder
import io.gatling.http.request.builder.CommonAttributes

/**
 * @author ctranxuan
 */
object Sse {
  val DefaultSseName = SessionPrivateAttributes.PrivateAttributePrefix + "http.sse"

}

class Sse(requestName: Expression[String], sseName: String = Sse.DefaultSseName) {

  def sseName(sseName: String) = new Sse(requestName, sseName)

  def open(url: Expression[String]) = new SseOpenRequestBuilder(CommonAttributes(requestName, "GET", Left(url)), sseName)

  def close() = new SseCloseActionBuilder(requestName, sseName)

}
