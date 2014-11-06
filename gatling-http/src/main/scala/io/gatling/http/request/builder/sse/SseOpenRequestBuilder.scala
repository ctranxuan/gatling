package io.gatling.http.request.builder.sse

import com.ning.http.client.Request
import io.gatling.core.session.Expression
import io.gatling.http.action.sse.SseOpenActionBuilder
import io.gatling.http.config.HttpProtocol
import io.gatling.http.request.builder.{ RequestBuilder, CommonAttributes }

/**
 * @author ctranxuan
 */
object SseOpenRequestBuilder {

  implicit def toActionBuilder(requestBuilder: SseOpenRequestBuilder) = new SseOpenActionBuilder(requestBuilder.commonAttributes.requestName, requestBuilder.sseName, requestBuilder)
}

class SseOpenRequestBuilder(commonAttributes: CommonAttributes, val sseName: String) extends RequestBuilder[SseOpenRequestBuilder](commonAttributes) {

  override private[http] def newInstance(commonAttributes: CommonAttributes): SseOpenRequestBuilder = new SseOpenRequestBuilder(commonAttributes, sseName)

  def build(protocol: HttpProtocol): Expression[Request] = new SseRequestExpressionBuilder(commonAttributes, protocol).build
}
