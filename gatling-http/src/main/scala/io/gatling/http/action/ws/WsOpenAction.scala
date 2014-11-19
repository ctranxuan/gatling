/**
 * Copyright 2011-2014 eBusiness Information, Groupe Excilys (www.excilys.com)
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
package io.gatling.http.action.ws

import com.ning.http.client.Request

import akka.actor.ActorDSL.actor
import akka.actor.ActorRef
import io.gatling.core.action.Interruptable
import io.gatling.core.session.{ Expression, Session }
import io.gatling.core.util.TimeHelper.nowMillis
import io.gatling.http.ahc.{ HttpEngine, WsTx }
import io.gatling.http.check.ws._
import io.gatling.http.config.HttpProtocol

class WsOpenAction(
    requestName: Expression[String],
    wsName: String,
    request: Expression[Request],
    checkBuilder: Option[WsCheckBuilder],
    val next: ActorRef,
    protocol: HttpProtocol) extends Interruptable {

  def execute(session: Session): Unit = {

      def open(tx: WsTx): Unit = {
        logger.info(s"Opening websocket '$wsName': Scenario '${session.scenarioName}', UserId #${session.userId}")

        val wsActor = actor(context)(new WsActor(wsName))

        HttpEngine.instance.startWebSocketTransaction(tx, wsActor)
      }

    for {
      requestName <- requestName(session)
      request <- request(session)
      check = checkBuilder.map(_.build)
    } yield open(WsTx(session, request, requestName, protocol, next, nowMillis, check = check))
  }
}
