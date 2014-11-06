package io.gatling.http.action.sse

/**
 * @author ctranxuan
 */
trait SseEmitter {
  def stopEmitting(): Unit;
}
