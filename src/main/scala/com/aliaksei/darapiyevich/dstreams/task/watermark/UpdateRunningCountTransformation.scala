package com.aliaksei.darapiyevich.dstreams.task.watermark

import java.time.LocalDateTime
import java.time.temporal.{TemporalAmount, TemporalUnit}

import org.apache.spark.streaming.dstream.DStream

import scala.reflect.ClassTag

/**
  * Function to count input by keys within window with watermarks.
  * Event is considered as relevant when "(window close time + watermark) < now"
  * Usage:
  * 1. import [[com.aliaksei.darapiyevich.dstreams.task.watermark.UpdateRunningCountTransformation#stream2updateRunningCountTransformation]]
  * 2. setup function using methods available on
  * [[com.aliaksei.darapiyevich.dstreams.task.watermark.UpdateRunningCountTransformationBuilder]]
  *
  * @tparam I DStream input element
  * @tparam K key to count elements by
  */
class UpdateRunningCountTransformation[I, K] private[watermark](input: DStream[I],
                                                                makeKeyEventTimeValuePair: I => (K, LocalDateTime),
                                                                updateRunningCountFunction: (Seq[LocalDateTime], Option[RunningCount]) => Option[RunningCount]
                                                               )
                                                               (implicit iTag: ClassTag[I],
                                                                kTag: ClassTag[K]
                                                               ) {

  def count(): DStream[(K, Long)] = {
    val localMakeKV = makeKeyEventTimeValuePair
    val localUpdateFunction = updateRunningCountFunction

    input.map(localMakeKV)
      .updateStateByKey(localUpdateFunction)
      .filter {
        case (_, runningCount) => runningCount.updated
      }
      .map {
        case (key, runningCount) => (key, runningCount.value)
      }
  }
}

class UpdateRunningCountTransformationBuilder[I] private[watermark](
                                                                     stream: DStream[I]
                                                                   ) {
  private var window: TemporalUnit = _
  private var watermark: TemporalAmount = _

  def withinWindow(window: TemporalUnit, watermark: TemporalAmount): UpdateRunningCountTransformationBuilder[I] = {
    this.window = window
    this.watermark = watermark
    this
  }

  /**
    * @param key       function to get object which contains "eventTime" truncated to window with other properties
    *                  needed to calculate count by
    * @param eventTime function to get "eventTime" of input element
    */

  def on[K](key: I => K, eventTime: I => LocalDateTime)
           (implicit iTag: ClassTag[I], kTag: ClassTag[K]): UpdateRunningCountTransformation[I, K] = {
    val makeKeyEventTimePair = (record: I) => (key(record), eventTime(record))
    val updateRunningCount = new UpdateRunningCountFunction(window, watermark)
    new UpdateRunningCountTransformation[I, K](stream, makeKeyEventTimePair, updateRunningCount)
  }
}

object UpdateRunningCountTransformation {
  implicit def stream2updateRunningCountTransformation[I](stream: DStream[I]): UpdateRunningCountTransformationBuilder[I] = {
    new UpdateRunningCountTransformationBuilder[I](stream)
  }
}