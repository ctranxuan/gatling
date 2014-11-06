package io.gatling.http.action.sse

import akka.actor.ActorDSL.actor
import akka.actor.ActorRef
import com.ning.http.client.Request
import io.gatling.core.action.Interruptable
import io.gatling.core.session.{Expression, Session}
import io.gatling.core.util.TimeHelper.nowMillis
import io.gatling.http.ahc.{HttpEngine, SseTx}
import io.gatling.http.config.HttpProtocol
import io.gatling.http.request.builder.sse.SseCheck

/**
 * @author ctranxuan
 */
class SseOpenAction(
    requestName: Expression[String],
    sseName: String,
    request: Expression[Request],
    check: Option[SseCheck],
    val next: ActorRef,
    protocol: HttpProtocol) extends Interruptable {

  override def execute(session: Session): Unit = {

      def open(tx: SseTx): Unit = {
        logger.info(s"Opening sse '$sseName': Scenario '${session.scenarioName}', UserId #${session.userId}")

        val sseActor = actor(context)(new SseActor(sseName))

        HttpEngine.instance.startSseTransaction(tx, sseActor)
      }

    for {
      requestName <- requestName(session)
      request <- request(session)
    } yield open(SseTx(session, request, requestName, protocol, next, nowMillis, check = check))
  }
}
