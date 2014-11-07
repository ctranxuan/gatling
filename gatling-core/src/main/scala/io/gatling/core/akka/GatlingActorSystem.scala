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
package io.gatling.core.akka

import akka.actor.ActorSystem
import com.typesafe.scalalogging.StrictLogging

object GatlingActorSystem extends StrictLogging {

  var instanceOpt: Option[ActorSystem] = None

  def start(): ActorSystem = synchronized {
    instanceOpt match {
      case None =>
        val system = ActorSystem("GatlingSystem")
        instanceOpt = Some(system)
        system

      case _ => throw new UnsupportedOperationException("Gatling Actor system is already started")
    }
  }

  def instance = instanceOpt match {
    case Some(a) => a
    case None    => throw new UnsupportedOperationException("Gatling Actor system hasn't been started")
  }

  def shutdown(): Unit = synchronized {
    instanceOpt match {
      case Some(system) =>
        system.shutdown()
        system.awaitTermination()
        instanceOpt = None

      case None =>
        logger.warn("Gatling Actor system wasn't shut down as it wasn't started in the first place")
    }
  }
}
