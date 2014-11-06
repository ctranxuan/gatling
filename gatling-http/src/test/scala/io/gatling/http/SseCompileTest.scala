package io.gatling.http

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

/**
 * @author ctranxuan
 */
class SseCompileTest extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:8080")
    .acceptHeader("text/event-stream")
    //    .acceptLanguageHeader("en-US,en;q=0.5")
    //    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
  //    .wsReconnect // FIXME
  //    .wsMaxReconnects(3) // FIXME

  val scn = scenario("Sse")
    .exec(sse("StockMarket").open("/stocks/prices") //          .check(// FIXME)
    )

  setUp(scn.inject(rampUsers(100) over 10)).protocols(httpConf)
}