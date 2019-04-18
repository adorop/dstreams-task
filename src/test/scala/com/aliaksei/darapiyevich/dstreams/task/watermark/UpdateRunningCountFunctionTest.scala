package com.aliaksei.darapiyevich.dstreams.task.watermark

import java.time.temporal.ChronoUnit
import java.time.{Duration, LocalDate, LocalDateTime, LocalTime}

import com.aliaksei.darapiyevich.dstreams.task.UnitTestSpec

class UpdateRunningCountFunctionTest extends UnitTestSpec {
  private val runningCountValue = 999

  private val today = LocalDate.now
  private val fixedNow = LocalDateTime.of(today, LocalTime.of(12, 0, 1))
  private val watermark = Duration.ofHours(1)
  private val windowLength = ChronoUnit.HOURS

  private val closedWindowWithPastWatermark = LocalDateTime.of(today, LocalTime.of(11, 0, 0))
  private val lateValue = LocalDateTime.of(today, LocalTime.of(10, 59, 59))
  private val closedWindowWithPendingWatermark = LocalDateTime.of(today, LocalTime.of(12, 0, 0))
  private val actualValue = LocalDateTime.of(today, LocalTime.of(11, 0, 0))

  private val updateRunningCountFunction = new UpdateRunningCountFunction(windowLength, watermark) {
    override private[watermark] def now = fixedNow
  }

  "update" should "return None when RunningCount.windowClosesAt + watermark is before now" in {
    val result = updateRunningCountFunction(Seq.empty, Some(RunningCount(runningCountValue, closedWindowWithPastWatermark)))
    result should not be defined
  }

  it should "return None when RunningCoutn is not defined and values are late" in {
    val result = updateRunningCountFunction(Seq(lateValue), None)
    result should not be defined
  }

  it should "return RunningCount updated by values' size when RunningCount.windowClosesAt + watermark is after now" in {
    val values = Seq.empty
    val result = updateRunningCountFunction(values, Some(RunningCount(runningCountValue, closedWindowWithPendingWatermark)))
      .get
    result.value shouldBe (values.size + runningCountValue)
    result.windowClosesAt shouldBe closedWindowWithPendingWatermark
  }

  it should "return initial RunningCount updated by values' size when RunningCount is not defined and values's window closes + watermark is after now" in {
    val values = Seq(actualValue)
    val result = updateRunningCountFunction(values, None).get
    result.value shouldBe updateRunningCountFunction.initialCount + values.size
    result.windowClosesAt shouldEqual expectedWindowClosesAt
  }

  private def expectedWindowClosesAt: LocalDateTime = {
    actualValue.plus(1, windowLength).truncatedTo(windowLength)
  }
}
