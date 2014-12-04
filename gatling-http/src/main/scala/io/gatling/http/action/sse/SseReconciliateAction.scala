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

import akka.actor.ActorRef
import io.gatling.core.session.{ Session, Expression }
import io.gatling.core.validation.Validation
import io.gatling.http.action.RequestAction

class SseReconciliateAction(val requestName: Expression[String], sseName: String, val next: ActorRef)
    extends RequestAction
    with SseAction {

  override def sendRequest(requestName: String,
                           session: Session): Validation[Unit] = {
    for {
      sseActor <- fetchSse(sseName, session)
    } yield sseActor ! Reconciliate(requestName, next, session)
  }
}
