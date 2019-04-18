package com.aliaksei.darapiyevich.dstreams.task.kafka.serialization

import org.apache.kafka.common.header.Headers
import org.springframework.kafka.support.serializer.JsonDeserializer

abstract class KafkaJsonDeserializerTemplate[T] extends JsonDeserializer[T] {
  targetType = clazz
  private val objectReader = objectMapper.readerFor(targetType)

  def clazz: Class[T]

  override def deserialize(topic: String, headers: Headers, data: Array[Byte]): T = {
    objectReader.readValue(data)
  }
}
