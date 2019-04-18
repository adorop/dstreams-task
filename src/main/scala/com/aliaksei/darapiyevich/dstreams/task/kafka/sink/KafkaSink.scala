package com.aliaksei.darapiyevich.dstreams.task.kafka.sink

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.dstream.DStream

class KafkaSink[E](stream: DStream[E]) {

  def saveToKafka(topic: String, kafkaTemplate: Broadcast[KafkaTemplate]): Unit = {
    stream.foreachRDD {
      rdd =>
        rdd.foreach {
          e =>
            kafkaTemplate.value
              .send(topic, e)
        }
    }
  }

}

object KafkaSink {
  implicit def stream2kafkaSink[E](stream: DStream[E]): KafkaSink[E] = {
    new KafkaSink[E](stream)
  }
}