package com.aliaksei.darapiyevich.dstreams.task.functions

import java.time.{LocalDateTime, ZoneId}
import java.util.Date

object utils {
  implicit def date2LocalDateTime(date: Date): LocalDateTime = {
    date.toInstant
      .atZone(ZoneId.of("UTC"))
      .toLocalDateTime
  }

}
