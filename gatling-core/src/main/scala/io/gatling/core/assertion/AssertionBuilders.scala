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

class AssertionWithPath(path: Path) {

  def responseTime = new AssertionWithPathAndTimeMetric(path, ResponseTime)
  def allRequests = new AssertionWithPathAndCountMetric(path, AllRequests)
  def failedRequests = new AssertionWithPathAndCountMetric(path, FailedRequests)
  def successfulRequests = new AssertionWithPathAndCountMetric(path, SuccessfulRequests)
  def requestsPerSec = new AssertionWithPathAndTarget(path, MeanRequestsPerSecondTarget)
}

class AssertionWithPathAndTimeMetric(path: Path, metric: TimeMetric) {

  private def next(selection: TimeSelection) =
    new AssertionWithPathAndTarget(path, TimeTarget(metric, selection))

  def min = next(Min)
  def max = next(Max)
  def mean = next(Mean)
  def stdDev = next(StandardDeviation)
  def percentile1 = next(Percentiles1)
  def percentile2 = next(Percentiles2)
}

class AssertionWithPathAndCountMetric(path: Path, metric: CountMetric) {

  private def next(selection: CountSelection) =
    new AssertionWithPathAndTarget(path, CountTarget(metric, selection))

  def count = next(Count)
  def percent = next(Percent)
}

class AssertionWithPathAndTarget(path: Path, target: Target) {

  def next(condition: Condition) =
    Assertion(path, target, condition)

  def lessThan(threshold: Double) = next(LessThan(threshold))
  def greaterThan(threshold: Double) = next(GreaterThan(threshold))
  def between(min: Double, max: Double) = next(Between(min, max))
  def is(value: Double) = next(Is(value))
  def in(set: Set[Double]) = next(In(set.toList))
}
