package io.gatling.http.action.sse

import org.scalatest.{FlatSpec, Matchers}

/**
 * @author ctranxuan
 */
class ServerSentEventSpec extends FlatSpec with Matchers {

  val sseParser = ServerSentEventParser



  "completeSse" should "return a server sent event with a snapshot type" in {
    val completeSse = """event: snapshot
                    id: 4d80cbbb-d456-4268-816a-0f8e411eec42
                    data: [{"title":"Value 1","price":91,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 2","price":52,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 3","price":64,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 4","price":10,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 5","price":67,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 6","price":86,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 7","price":40,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 8","price":91,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 9","price":1,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 10","price":95,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 11","price":91,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 12","price":13,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 13","price":52,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 14","price":24,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 15","price":30,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"}]
                    retry: 1200
                   """

    val eventType = "snapshot"
    val id = "4d80cbbb-d456-4268-816a-0f8e411eec42"
    val data = "[{\"title\":\"Value 1\",\"price\":91,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 2\",\"price\":52,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 3\",\"price\":64,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 4\",\"price\":10,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 5\",\"price\":67,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 6\",\"price\":86,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 7\",\"price\":40,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 8\",\"price\":91,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 9\",\"price\":1,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 10\",\"price\":95,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 11\",\"price\":91,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 12\",\"price\":13,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 13\",\"price\":52,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 14\",\"price\":24,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 15\",\"price\":30,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"}]"
    val retry = 1200

    val sse = sseParser(completeSse)
    sse.data shouldBe data
    sse.eventType shouldBe Some(eventType)
    sse.id shouldBe Some(id)
    sse.retry shouldBe Some(retry)
  }


  "sseNoRetry" should "return a server sent event with a snapshot type with no retry" in {
    val ssseNoRetry = """event: snapshot
                    id: 4d80cbbb-d456-4268-816a-0f8e411eec42
                    data: [{"title":"Value 1","price":91,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 2","price":52,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 3","price":64,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 4","price":10,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 5","price":67,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 6","price":86,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 7","price":40,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 8","price":91,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 9","price":1,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 10","price":95,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 11","price":91,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 12","price":13,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 13","price":52,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 14","price":24,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 15","price":30,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"}]
                      """
    val eventType = "snapshot"
    val id = "4d80cbbb-d456-4268-816a-0f8e411eec42"
    val data = "[{\"title\":\"Value 1\",\"price\":91,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 2\",\"price\":52,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 3\",\"price\":64,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 4\",\"price\":10,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 5\",\"price\":67,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 6\",\"price\":86,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 7\",\"price\":40,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 8\",\"price\":91,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 9\",\"price\":1,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 10\",\"price\":95,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 11\",\"price\":91,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 12\",\"price\":13,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 13\",\"price\":52,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 14\",\"price\":24,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 15\",\"price\":30,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"}]"

    val sse = sseParser(ssseNoRetry)
    sse.data shouldBe data
    sse.eventType shouldBe Some(eventType)
    sse.id shouldBe Some(id)
    sse.retry shouldBe None
  }



  "sseNoRetryNoId" should "return a server sent event with a snapshot type with no retry and no id" in {
    val sseNoRetryNoId = """event: snapshot
                        data: [{"title":"Value 1","price":91,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 2","price":52,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 3","price":64,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 4","price":10,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 5","price":67,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 6","price":86,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 7","price":40,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 8","price":91,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 9","price":1,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 10","price":95,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 11","price":91,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 12","price":13,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 13","price":52,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 14","price":24,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 15","price":30,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"}]
                         """
    val eventType = "snapshot"
    val data = "[{\"title\":\"Value 1\",\"price\":91,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 2\",\"price\":52,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 3\",\"price\":64,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 4\",\"price\":10,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 5\",\"price\":67,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 6\",\"price\":86,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 7\",\"price\":40,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 8\",\"price\":91,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 9\",\"price\":1,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 10\",\"price\":95,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 11\",\"price\":91,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 12\",\"price\":13,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 13\",\"price\":52,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 14\",\"price\":24,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 15\",\"price\":30,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"}]"

    val sse = sseParser(sseNoRetryNoId)
    sse.data shouldBe data
    sse.eventType shouldBe Some(eventType)
    sse.id shouldBe None
    sse.retry shouldBe None
  }


  "sseOnlyData" should "return a server sent event with only data" in {
    val sseOnlyData = """data: [{"title":"Value 1","price":91,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 2","price":52,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 3","price":64,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 4","price":10,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 5","price":67,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 6","price":86,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 7","price":40,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 8","price":91,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 9","price":1,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 10","price":95,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 11","price":91,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 12","price":13,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 13","price":52,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 14","price":24,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 15","price":30,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"}]"""
    val data = "[{\"title\":\"Value 1\",\"price\":91,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 2\",\"price\":52,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 3\",\"price\":64,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 4\",\"price\":10,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 5\",\"price\":67,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 6\",\"price\":86,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 7\",\"price\":40,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 8\",\"price\":91,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 9\",\"price\":1,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 10\",\"price\":95,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 11\",\"price\":91,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 12\",\"price\":13,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 13\",\"price\":52,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 14\",\"price\":24,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 15\",\"price\":30,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"}]"

    val sse = sseParser(sseOnlyData)
    sse.data shouldBe data
    sse.eventType shouldBe None
    sse.id shouldBe None
    sse.retry shouldBe None
  }

  "sseWithExtraFields" should "return a server sent event with legacy fields" in {
    val sseWithExtraFields = """event: snapshot
                    id: 4d80cbbb-d456-4268-816a-0f8e411eec42
                    data: [{"title":"Value 1","price":91,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 2","price":52,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 3","price":64,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 4","price":10,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 5","price":67,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 6","price":86,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 7","price":40,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 8","price":91,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 9","price":1,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 10","price":95,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 11","price":91,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 12","price":13,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 13","price":52,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 14","price":24,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 15","price":30,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"}]
                    retry: 1200
                    foo: bar
                    fooz
                      """

    val eventType = "snapshot"
    val id = "4d80cbbb-d456-4268-816a-0f8e411eec42"
    val data = "[{\"title\":\"Value 1\",\"price\":91,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 2\",\"price\":52,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 3\",\"price\":64,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 4\",\"price\":10,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 5\",\"price\":67,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 6\",\"price\":86,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 7\",\"price\":40,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 8\",\"price\":91,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 9\",\"price\":1,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 10\",\"price\":95,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 11\",\"price\":91,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 12\",\"price\":13,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 13\",\"price\":52,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 14\",\"price\":24,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 15\",\"price\":30,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"}]"
    val retry = 1200

    val sse = sseParser(sseWithExtraFields)
    sse.data shouldBe data
    sse.eventType shouldBe Some(eventType)
    sse.id shouldBe Some(id)
    sse.retry shouldBe Some(retry)
  }

  "sseWithComments" should "return a server sent event with legacy fields" in {
    val sseWithComments = """
                    : This is a begin comment
                    event: snapshot
                    id: 4d80cbbb-d456-4268-816a-0f8e411eec42
                    : This is a comment
                    data: [{"title":"Value 1","price":91,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 2","price":52,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 3","price":64,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 4","price":10,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 5","price":67,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 6","price":86,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 7","price":40,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 8","price":91,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 9","price":1,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 10","price":95,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 11","price":91,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 12","price":13,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 13","price":52,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 14","price":24,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"},{"title":"Value 15","price":30,"param1":"value1","param2":"value2","param3":"value3","param4":"value4","param5":"value5","param6":"value6","param7":"value7","param8":"value8"}]
                    retry: 1200
                    : This is an end comment
                             """

    val eventType = "snapshot"
    val id = "4d80cbbb-d456-4268-816a-0f8e411eec42"
    val data = "[{\"title\":\"Value 1\",\"price\":91,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 2\",\"price\":52,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 3\",\"price\":64,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 4\",\"price\":10,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 5\",\"price\":67,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 6\",\"price\":86,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 7\",\"price\":40,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 8\",\"price\":91,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 9\",\"price\":1,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 10\",\"price\":95,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 11\",\"price\":91,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 12\",\"price\":13,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 13\",\"price\":52,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 14\",\"price\":24,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"},{\"title\":\"Value 15\",\"price\":30,\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":\"value3\",\"param4\":\"value4\",\"param5\":\"value5\",\"param6\":\"value6\",\"param7\":\"value7\",\"param8\":\"value8\"}]"
    val retry = 1200

    val sse = sseParser(sseWithComments)
    sse.data shouldBe data
    sse.eventType shouldBe Some(eventType)
    sse.id shouldBe Some(id)
    sse.retry shouldBe Some(retry)
  }
}
