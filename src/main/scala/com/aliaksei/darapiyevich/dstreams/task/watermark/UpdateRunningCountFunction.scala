package com.aliaksei.darapiyevich.dstreams.task.watermark

import java.time.LocalDateTime
import java.time.temporal.{TemporalAmount, TemporalUnit}

class UpdateRunningCountFunction(
                                  windowLength: TemporalUnit,
                                  watermark: TemporalAmount
                                ) extends ((Seq[LocalDateTime], Option[RunningCount]) => Option[RunningCount]) with Serializable {
  private[watermark] val initialCount = 0

  override def apply(values: Seq[LocalDateTime], runningCount: Option[RunningCount]): Option[RunningCount] = {
    runningCount
      .orElse(initialCount(values))
      .filter(thresholdIsNotReached)
      .map(_ + values.size)
  }

  private def initialCount(values: Seq[LocalDateTime]): Option[RunningCount] = {
    Some(RunningCount(initialCount, windowClosesAt(values.head)))
  }

  private def windowClosesAt(time: LocalDateTime): LocalDateTime = {
    time.plus(1, windowLength)
      .truncatedTo(windowLength)
  }

  private def thresholdIsNotReached(runningCount: RunningCount): Boolean = {
    !runningCount.windowClosesAt.plus(watermark).isBefore(now)
  }

  private[watermark] def now: LocalDateTime = {
    LocalDateTime.now
  }
}
