package io.gatling.core.assertion

import io.gatling.core.result.reader.DataReader

object AssertionValidator {

  def validateAssertions(dataReader: DataReader): Boolean = ???
  //  def pathPath
  /*
  * val global = new Selector((reader, status) => reader.requestGeneralStats(None, None, status).success, "Global")

  def details(selector: AssertionPath): Selector = {

      def generalStats(selector: AssertionPath): (DataReader, Option[Status]) => Validation[GeneralStats] = (reader, status) =>
        if (selector.parts.isEmpty)
          reader.requestGeneralStats(None, None, status).success

        else {
          val selectedPath: List[String] = selector.parts
          val foundPath = reader.statsPaths.find { statsPath =>
            val path: List[String] = statsPath match {
              case RequestStatsPath(request, group) =>
                group match {
                  case Some(g) => g.hierarchy :+ request
                  case _       => List(request)
                }
              case GroupStatsPath(group) => group.hierarchy
            }
            path == selectedPath
          }

          foundPath match {
            case None                                   => s"Could not find stats matching selector $selector".failure
            case Some(RequestStatsPath(request, group)) => reader.requestGeneralStats(Some(request), group, status).success
            case Some(GroupStatsPath(group))            => reader.requestGeneralStats(None, Some(group), status).success
          }
        }

    new Selector(generalStats(selector), selector.parts.mkString(" / "))
  }*/

  /*
  class Selector(stats: (DataReader, Option[Status]) => Validation[GeneralStats], name: String) {

  def responseTime = new ResponseTime(reader => stats(reader, None), name)

  def allRequests = new Requests(stats, None, name)

  def failedRequests = new Requests(stats, Some(KO), name)

  def successfulRequests = new Requests(stats, Some(OK), name)

  def requestsPerSec = Metric(reader => stats(reader, None).map(_.meanRequestsPerSec), s"$name: requests per second")
}

object ResponseTime {
  val Percentile1 = configuration.charting.indicators.percentile1.toRank
  val Percentile2 = configuration.charting.indicators.percentile2.toRank
}

class ResponseTime(responseTime: DataReader => Validation[GeneralStats], name: String) {
  def min = Metric(reader => responseTime(reader).map(_.min), s"$name: min response time")

  def max = Metric(reader => responseTime(reader).map(_.max), s"$name: max response time")

  def mean = Metric(reader => responseTime(reader).map(_.mean), s"$name: mean response time")

  def stdDev = Metric(reader => responseTime(reader).map(_.stdDev), s"$name: standard deviation response time")

  def percentile1 = Metric(reader => responseTime(reader).map(_.percentile1), s"$name: ${ResponseTime.Percentile1} percentile response time")

  def percentile2 = Metric(reader => responseTime(reader).map(_.percentile2), s"$name: ${ResponseTime.Percentile2} percentile response time")
}

class Requests(requests: (DataReader, Option[Status]) => Validation[GeneralStats], status: Option[Status], name: String) {

  private def message(message: String) = status match {
    case Some(s) => s"$name $message $s"
    case None    => s"$name $message"
  }

  def percent = {
    val value = (reader: DataReader) => for {
      statusStats <- requests(reader, status)
      allStats <- requests(reader, None)
    } yield math.round(statusStats.count.toFloat / allStats.count * 100)

    Metric(value, message("percentage of requests"))
  }

  def count = Metric(reader => requests(reader, status).map(_.count), message("number of requests"))
}

case class Metric[T: Numeric](value: DataReader => Validation[T], name: String, assertions: List[Assertion] = List()) {

  def assert(assertion: (T) => Boolean, message: (String, Boolean) => String) = {
    val newAssertion = new Assertion(
      reader => value(reader).map(assertion),
      result => message(name, result))
    copy(assertions = assertions :+ newAssertion)
  }

  def lessThan(threshold: T) = assert(implicitly[Numeric[T]].lt(_, threshold), (name, result) => s"$name is less than $threshold: $result")

  def greaterThan(threshold: T) = assert(implicitly[Numeric[T]].gt(_, threshold), (name, result) => s"$name is greater than $threshold: $result")

  def between(min: T, max: T) = assert(v => implicitly[Numeric[T]].gteq(v, min) && implicitly[Numeric[T]].lteq(v, max), (name, result) => s"$name between $min and $max: $result")

  def is(v: T): Metric[T] = assert(_ == v, (name, result) => s"$name is equal to $v: $result")

  def in(set: Set[T]) = assert(set.contains, (name, result) => s"$name is in $set")
}

object Assertion {
  def assertThat(assertions: Seq[Assertion], dataReader: DataReader): Boolean =
    !assertions.map { assertion =>
      assertion(dataReader) match {
        case Success(result) =>
          println(assertion.message(result))
          result

        case Failure(m) =>
          println(m)
          false
      }
    }.contains(false)
}

case class Assertion(assertion: (DataReader) => Validation[Boolean], message: Boolean => String) {
  def apply(reader: DataReader): Validation[Boolean] = assertion(reader)
}
   */
}
