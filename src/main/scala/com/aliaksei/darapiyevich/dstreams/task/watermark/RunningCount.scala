package com.aliaksei.darapiyevich.dstreams.task.watermark

import java.time.LocalDateTime

case class RunningCount(
                         value: Long,
                         windowClosesAt: LocalDateTime,
                         updated: Boolean = true
                       ) {

  def +(increaseBy: Long): RunningCount = {
    RunningCount(value + increaseBy, windowClosesAt, updated = increaseBy != 0)
  }
}
