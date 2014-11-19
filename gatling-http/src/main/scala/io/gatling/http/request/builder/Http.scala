package io.gatling.http.request.builder

import com.ning.http.client.uri.Uri
import io.gatling.core.session.Expression

/**
 * @param requestName the name of the request
 */
class Http(requestName: Expression[String]) {

  def get(url: Expression[String]) = httpRequest("GET", url)
  def get(uri: Uri) = httpRequest("GET", Right(uri))
  def put(url: Expression[String]) = httpRequest("PUT", url)
  def post(url: Expression[String]) = httpRequest("POST", url)
  def patch(url: Expression[String]) = httpRequest("PATCH", url)
  def head(url: Expression[String]) = httpRequest("HEAD", url)
  def delete(url: Expression[String]) = httpRequest("DELETE", url)
  def options(url: Expression[String]) = httpRequest("OPTIONS", url)
  def httpRequest(method: String, url: Expression[String]): HttpRequestBuilder = httpRequest(method, Left(url))
  def httpRequest(method: String, urlOrURI: Either[Expression[String], Uri]): HttpRequestBuilder = new HttpRequestBuilder(CommonAttributes(requestName, method, urlOrURI), HttpAttributes())
}
