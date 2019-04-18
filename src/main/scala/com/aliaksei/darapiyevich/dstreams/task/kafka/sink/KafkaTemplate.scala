package com.aliaksei.darapiyevich.dstreams.task.kafka.sink

import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import org.apache.spark.internal.Logging

import scala.collection.JavaConverters._

class KafkaTemplate private[kafka](createProducer: () => KafkaProducer[Any, Any]) extends Serializable with Logging {
  @transient
  lazy val producer = createProducer()

  def send(topic: String, value: Any): Unit = {
    logInfo(s"Value to send: $value")
    try {
      producer.send(new ProducerRecord[Any, Any](topic, value), new LogErrorCallback())
    } catch {
      case e: Throwable => logError(s"Failed to send a message: $value", e)
    }
  }

}

class LogErrorCallback extends Callback with Logging {
  override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
    if (exception != null) {
      logError("Failed to send a message", exception)
    }
  }
}

object KafkaTemplate {

  def apply(config: Map[String, AnyRef]): KafkaTemplate = {
    val createProducer = () => {
      val producer = new KafkaProducer[Any, Any](config.asJava)
      sys.addShutdownHook(producer.close())
      producer
    }
    new KafkaTemplate(createProducer)
  }

}