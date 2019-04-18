package com.aliaksei.darapiyevich.dstreams.task.kafka.sink

import com.aliaksei.darapiyevich.dstreams.task.UnitTestSpec
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.mockito.Matchers
import org.mockito.Mockito._

class KafkaTemplateTest extends UnitTestSpec {
  private val topic = "topic"
  private val messageValue = 1

  private trait Setup {
    val producer: KafkaProducer[Any, Any] = mock[KafkaProducer[Any, Any]]
    val createProducer: () => KafkaProducer[Any, Any] = () => producer
    val kafkaTemplate: KafkaTemplate = new KafkaTemplate(createProducer)
  }

  "send" should "send message to given topic with given value" in new Setup {
    kafkaTemplate.send(topic, messageValue)
    verify(producer).send(
      Matchers.eq(new ProducerRecord[Any, Any](topic, messageValue)),
      Matchers.any(classOf[LogErrorCallback])
    )
  }
}
