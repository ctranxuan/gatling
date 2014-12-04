/**
 * Copyright 2011-2014 eBusiness Information, Groupe Excilys (www.ebusinessinformation.fr)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gatling.http.action.sse

import akka.actor.ActorDSL.actor
import akka.actor.ActorRef
import com.ning.http.client.Request
import io.gatling.core.action.Interruptable
import io.gatling.core.session.{ Expression, Session }
import io.gatling.core.util.TimeHelper.nowMillis
import io.gatling.http.ahc.{ HttpEngine, SseTx }
import io.gatling.http.check.sse.SseCheck
import io.gatling.http.config.HttpProtocol

class SseGetAction(
    requestName: Expression[String],
    sseName: String,
    request: Expression[Request],
    check: Option[SseCheck],
    val next: ActorRef,
    protocol: HttpProtocol) extends Interruptable {

  override def execute(session: Session): Unit = {

      def get(tx: SseTx): Unit = {
        logger.info(s"Opening and getting sse '$sseName': Scenario '${session.scenarioName}', UserId #${session.userId}")
        val sseActor = actor(context)(new SseActor(sseName))
        HttpEngine.instance.startSseTransaction(tx, sseActor)
      }

    for {
      requestName <- requestName(session)
      request <- request(session)
    } yield get(SseTx(session, request, requestName, protocol, next, nowMillis, check = check))
  }
}
