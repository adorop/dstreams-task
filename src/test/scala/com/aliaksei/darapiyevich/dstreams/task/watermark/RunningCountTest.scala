package com.aliaksei.darapiyevich.dstreams.task.watermark

import java.time.LocalDateTime

import com.aliaksei.darapiyevich.dstreams.task.UnitTestSpec

class RunningCountTest extends UnitTestSpec {
  private val increaseBy = 10
  private val initialValue = 0
  private val initialWindowClosesAt = LocalDateTime.now

  private val initial = RunningCount(initialValue, initialWindowClosesAt)

  "+" should "return RunningCount with value increased by given argument" in {
    val result = initial + increaseBy
    result.value shouldBe initialValue + increaseBy
  }

  it should "return RunningCoutn with same windowClosesAt as initial one" in {
    val result = initial + increaseBy
    result.windowClosesAt shouldBe initialWindowClosesAt
  }

  it should "return 'updated' RunningCount when increaseBy is not zero" in {
    val result = initial + 1
    result.updated shouldBe true
  }

  it should "return 'not updated' RunningCoutn when increaseBy is zero" in {
    val result = initial + 0
    result.updated shouldBe false
  }
}
