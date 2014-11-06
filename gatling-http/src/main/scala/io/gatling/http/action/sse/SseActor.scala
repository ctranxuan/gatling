package io.gatling.http.action.sse

import akka.actor.ActorRef
import com.ning.http.client.ListenableFuture
import io.gatling.core.akka.BaseActor
import io.gatling.core.check.CheckResult
import io.gatling.core.result.message.{ KO, OK, Status }
import io.gatling.core.result.writer.DataWriterClient
import io.gatling.core.session.Session
import io.gatling.core.util.TimeHelper.nowMillis
import io.gatling.core.validation.{ Failure, Success }
import io.gatling.http.ahc.SseTx
import io.gatling.http.check.ws.{ ExpectedCount, ExpectedRange, UntilCount }

import scala.collection.mutable

/**
 * @author ctranxuan
 */
class SseActor(sseName: String) extends BaseActor with DataWriterClient {

  override def receive = initialState(None)

  def failPendingCheck(tx: SseTx, message: String): SseTx = {
    tx.check match {
      case Some(c) =>
        logRequest(tx.session, tx.requestName, KO, tx.start, nowMillis, Some(message))
        tx.copy(updates = Session.MarkAsFailedUpdate :: tx.updates, pendingCheckSuccesses = Nil, check = None)

      case _ => tx
    }
  }

  def initialState(future: Option[ListenableFuture[Unit]]): Receive = {
    case OnFuture(future) =>
      logger.error(s"Initiate state with future #${future.hashCode}")
      context.become(initialState(Some(future)))

    case OnSend(tx) =>
      import tx._
      logger.error(s"sse '$requestName' opened and request sent")

      val newSession = session.set(sseName, self)
      val newTx = tx.copy(session = newSession)

      context.become(openState(newTx, future))
      next ! newSession

    case OnFailedOpen(tx, message, end) =>
      import tx._
      logger.debug(s"sse '${sseName}' failed to open:$message")
      logRequest(session, requestName, KO, start, end, Some(message))

      next ! session.markAsFailed
      context.stop(self)

  }

  def openState(tx: SseTx, future: Option[ListenableFuture[Unit]]): Receive = {

      def stopEmitterAndSucceedPendingCheck(emitter: SseEmitter, results: List[CheckResult]): Unit = {
        emitter.stopEmitting
        succeedPendingCheck(results)
      }

      def succeedPendingCheck(results: List[CheckResult]): Unit = {
        tx.check match {
          case Some(check) =>
            // expected count met, let's stop the check
            logRequest(tx.session, tx.requestName, OK, tx.start, nowMillis, None)

            val checkResults = results.filter(_.hasUpdate)

            val newUpdates = checkResults match {
              case Nil =>
                // nothing to save, no update
                tx.updates

              case List(checkResult) =>
                // one single value to save
                checkResult.update.getOrElse(Session.Identity) :: tx.updates

              case _ =>
                // multiple values, let's pile them up
                val mergedCaptures = checkResults
                  .collect { case CheckResult(Some(value), Some(saveAs)) => saveAs -> value }
                  .groupBy(_._1)
                  .mapValues(_.flatMap(_._2 match {
                    case s: Seq[Any] => s
                    case v           => Seq(v)
                  }))

                val newUpdates = (session: Session) => session.setAll(mergedCaptures)
                newUpdates :: tx.updates
            }

            if (check.blocking) {
              // apply updates and release blocked flow
              val newSession = tx.session.update(newUpdates)

              tx.next ! newSession
              // todo check the start = nowMillis with an example
              val newTx = tx.copy(session = newSession, updates = Nil, check = None, pendingCheckSuccesses = Nil, start = nowMillis)
              context.become(openState(newTx, future))

            } else {
              // add to pending updates
              // todo check the start = nowMillis with an example
              val newTx = tx.copy(updates = newUpdates, check = None, pendingCheckSuccesses = Nil, start = nowMillis)
              context.become(openState(newTx, future))
            }

          case _ =>
        }
      }

      def reconciliate(next: ActorRef, session: Session): Unit = {
        val newTx = tx.applyUpdates(session)
        context.become(openState(newTx, future))
        next ! newTx.session
      }

    {
      case OnFuture(future) =>
        logger.error(s"Associate the future #${future.hashCode} to the user #${tx.session.userId}")
        context.become(openState(tx, Some(future)))

      case OnMessage(message, end, emitter) =>
        logger.error(s"Received message '$message' for user #${tx.session.userId}")
        import tx._

        tx.check match {
          case Some(c) =>
            implicit val cache = mutable.Map.empty[Any, Any]

            tx.check.foreach { check =>
              val validation = check.check(message, tx.session)

              validation match {
                case Success(result) =>
                  val results = result :: tx.pendingCheckSuccesses

                  check.expectation match {
                    case UntilCount(count) if count == results.length                          => stopEmitterAndSucceedPendingCheck(emitter, results)
                    case ExpectedCount(count) if count == results.length                       => stopEmitterAndSucceedPendingCheck(emitter, results)
                    case ExpectedRange(range) if range.contains(tx.pendingCheckSuccesses.size) => stopEmitterAndSucceedPendingCheck(emitter, results)
                    case _ =>
                      // let's pile up
                      logRequest(session, requestName, OK, start, end) // todo check with an example
                      val newTx = tx.copy(pendingCheckSuccesses = results, start = end)
                      context.become(openState(newTx, future))
                  }

                case Failure(error) =>
                  val newTx = failPendingCheck(tx, error)
                  context.become(openState(newTx, future))
              }
            }

          case None =>
            logRequest(session, requestName, OK, start, end)
            val newTx = tx.copy(start = end)
            context.become(openState(newTx, future))
        }

      case Reconciliate(requestName, next, session) =>
        logger.error("Reconciliate")
        logger.debug(s"Reconciliating sse '$sseName'")
        reconciliate(next, session)

      case Close(requestName, next, session) =>
        logger.error(s"Closing sse '$sseName' for user #${session.userId} with future #${future.hashCode}")

        future.foreach(f => f.done)

        val newTx = failPendingCheck(tx, "Check didn't succeed by the time the sse was asked to closed")
          .applyUpdates(session)
          .copy(requestName = requestName, start = nowMillis, next = next)

        logRequest(session, requestName, OK, nowMillis, nowMillis)
        next ! session.remove(sseName)
        context.stop(self)

      //        context.become(closingState(newTx))

      case OnThrowable(tx, message, end) => {
        logger.error("OnThrowable")
        import tx._
        logRequest(session, requestName, KO, start, end, Some(message))

        next ! session.remove(sseName)

        context.stop(self)
      }

      case unexpected =>
        logger.error("unexpected")
        logger.error(s"Discarding 1 unknown message $unexpected while in open state")
    }
  }

  def closingState(tx: SseTx): Receive = {
    case OnMessage(message, end, _) =>
      import tx._
      logRequest(session, requestName, OK, start, nowMillis)
      next ! session.remove(sseName)
      context.stop(self)

    case unexpected =>
      logger.error(s"Discarding 2 unknown message $unexpected while in closing state")
  }

  private def logRequest(session: Session, requestName: String, status: Status, started: Long, ended: Long, errorMessage: Option[String] = None): Unit = {
    writeRequestData(
      session,
      requestName,
      started,
      ended,
      ended,
      ended,
      status,
      errorMessage)
  }
}
