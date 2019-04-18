package com.aliaksei.darapiyevich.dstreams.task.functions

import java.time.LocalDateTime
import java.util.Date

import com.aliaksei.darapiyevich.dstreams.task.UnitTestSpec

class UtilsTest extends UnitTestSpec {

  "date2LocalDateTime" should "return the same time" in {
    val epoch = new Date()
    epoch.setTime(0)
    val result = utils.date2LocalDateTime(epoch)
    result shouldEqual LocalDateTime.of(1970, 1, 1, 0, 0, 0)
  }

}
