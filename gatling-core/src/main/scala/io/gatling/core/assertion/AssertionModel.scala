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
package io.gatling.core.assertion

import com.dongxiguo.fastring.Fastring
import com.dongxiguo.fastring.Fastring.Implicits._

import io.gatling.core.assertion.AssertionTags._

trait Serialized { def serialized: Fastring }

// ------------------- //
// -- Assertion ADT -- //
// ------------------- //

case class Assertion(path: Path, target: Target, condition: Condition) extends Serialized {
  val serialized = List(AssertionTag, PathTag, path.serialized, TargetTag, target.serialized, ConditionTag, condition.serialized).mkFastring("\t")
}

// -------------- //
// -- Path ADT -- //
// -------------- //

sealed trait Path extends Serialized
case object Global extends Path { val serialized = fast"$GlobalTag" }
case class Details(parts: List[String]) extends Path { val serialized = List(DetailsTag, parts.mkFastring("\t")).mkFastring("\t") }

// ---------------- //
// -- Metric ADT -- //
// ---------------- //

sealed abstract class Metric extends Serialized
sealed trait TimeMetric extends Metric
sealed trait CountMetric extends Metric

case object AllRequests extends CountMetric { val serialized = fast"$AllRequestsTag" }
case object FailedRequests extends CountMetric { val serialized = fast"$FailedRequestsTag" }
case object SuccessfulRequests extends CountMetric { val serialized = fast"$SuccessfulRequestsTag" }
case object ResponseTime extends TimeMetric { val serialized = fast"$ResponseTimeTag" }

// ------------------- //
// -- Selection ADT -- //
// ------------------- //

sealed trait Selection extends Serialized
sealed trait TimeSelection extends Selection
sealed trait CountSelection extends Selection

case object Count extends CountSelection { val serialized = fast"$CountTag" }
case object Percent extends CountSelection { val serialized = fast"$PercentTag" }
case object Min extends TimeSelection { val serialized = fast"$MinTag" }
case object Max extends TimeSelection { val serialized = fast"$MaxTag" }
case object Mean extends TimeSelection { val serialized = fast"$MeanTag" }
case object StandardDeviation extends TimeSelection { val serialized = fast"$StandardDeviationTag" }
case object Percentiles1 extends TimeSelection { val serialized = fast"$Percentiles1Tag" }
case object Percentiles2 extends TimeSelection { val serialized = fast"$Percentiles2Tag" }

// ---------------- //
// -- Target ADT -- //
// ---------------- //

sealed trait Target extends Serialized
case class CountTarget(metric: CountMetric, selection: CountSelection) extends Target {
  val serialized = List(metric, selection).map(_.serialized).mkFastring("\t")
}
case class TimeTarget(metric: TimeMetric, selection: TimeSelection) extends Target {
  val serialized = List(metric, selection).map(_.serialized).mkFastring("\t")
}
case object MeanRequestsPerSecondTarget extends Target { val serialized = fast"$MeanRequestsPerSecondTag" }

// ------------------- //
// -- Condition ADT -- //
// ------------------- //

sealed trait Condition extends Serialized
case class LessThan(value: Double) extends Condition { val serialized = List(LessThanTag, value).mkFastring("\t") }
case class GreaterThan(value: Double) extends Condition { val serialized = List(GreaterThanTag, value).mkFastring("\t") }
case class Is(value: Double) extends Condition { val serialized = List(IsTag, value).mkFastring("\t") }
case class Between(lowerBound: Double, upperBound: Double) extends Condition { val serialized = List(BetweenTag, lowerBound, upperBound).mkFastring("\t") }
case class In(elements: List[Double]) extends Condition { val serialized = List(InTag, elements.mkFastring("\t")).mkFastring("\t") }
