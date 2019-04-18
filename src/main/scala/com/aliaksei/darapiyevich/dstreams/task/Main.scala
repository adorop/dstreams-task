package com.aliaksei.darapiyevich.dstreams.task

import java.io.{FileInputStream, InputStreamReader}
import java.time
import java.time.temporal.ChronoUnit

import com.aliaksei.darapiyevich.dstreams.task.configuration.{ApplicationConfiguration, KafkaDestination, KafkaSource}
import com.aliaksei.darapiyevich.dstreams.task.functions.{CheckMandatoryProperties, ExplodeHashtags, MakeTweetsCountKey}
import com.aliaksei.darapiyevich.dstreams.task.kafka.serialization.KafkaJsonDeserializerTemplate
import com.aliaksei.darapiyevich.dstreams.task.kafka.sink.KafkaSink._
import com.aliaksei.darapiyevich.dstreams.task.kafka.sink.KafkaTemplate
import com.aliaksei.darapiyevich.dstreams.task.model.{Tweet, TweetsCount}
import com.aliaksei.darapiyevich.dstreams.task.watermark.UpdateRunningCountTransformation._
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Duration, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.read
import org.springframework.kafka.support.serializer.JsonSerializer

object Main {
  def main(args: Array[String]): Unit = {
    if (args.length != 1) {
      System.err.println("Path to ApplicationConfiguration json file is required")
      System.exit(-1)
    }

    val configuration = readConfiguration(args(0))
    val kafkaConsumerConfiguration = getConsumerConfiguration(configuration.kafkaSource)
    val kafkaProducerConfiguration = getProducerConfiguration(configuration.kafkaDestination)

    val createStreamingContext = () => {
      val ssc = {
        val ssc = new StreamingContext(new SparkConf(), Duration(configuration.microBatchDurationMillis))
        ssc.checkpoint(configuration.checkpointDirectory)
        ssc
      }

      val stream = KafkaUtils.createDirectStream[Any, Tweet](
        ssc,
        LocationStrategies.PreferConsistent,
        ConsumerStrategies.Subscribe[Any, Tweet](Array(configuration.kafkaSource.topic), kafkaConsumerConfiguration)
      )

      stream
        .map(_.value)
        .filter(new CheckMandatoryProperties)
        .flatMap(new ExplodeHashtags)
        .withinWindow(window = ChronoUnit.HOURS, watermark = time.Duration.of(1, ChronoUnit.HOURS))
        .on(key = new MakeTweetsCountKey, eventTime = _.getCreatedAt)
        .count()
        .map {
          case (key, count) => new TweetsCount(key, count)
        }
        .saveToKafka(configuration.kafkaDestination.topic, getKafkaTemplate(ssc.sparkContext, kafkaProducerConfiguration))
      ssc
    }

    val ssc = StreamingContext.getOrCreate(configuration.checkpointDirectory, createStreamingContext)
    ssc.start()
    ssc.awaitTermination()
  }

  private def readConfiguration(path: String): ApplicationConfiguration = {
    implicit val defaultFormats: DefaultFormats = DefaultFormats
    read[ApplicationConfiguration](new InputStreamReader(new FileInputStream(path)))
  }

  private def getConsumerConfiguration(kafkaSource: KafkaSource): Map[String, Object] = {
    Map(
      "bootstrap.servers" -> kafkaSource.bootstrapServer,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[JsonTweetDeserializer],
      "group.id" -> kafkaSource.consumerGroupId,
      "auto.offset.reset" -> kafkaSource.autoOffsetReset,
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
  }

  private def getProducerConfiguration(kafkaDestination: KafkaDestination): Map[String, String] = {
    Map(
      "bootstrap.servers" -> kafkaDestination.bootstrapServer,
      "key.serializer" -> classOf[StringSerializer].getName,
      "value.serializer" -> classOf[JsonSerializer[Tweet]].getName
    ) ++ kafkaDestination.options
  }

  private def getKafkaTemplate(spark: SparkContext, producerConfig: Map[String, String]): Broadcast[KafkaTemplate] = {
    spark.broadcast(KafkaTemplate(producerConfig))
  }
}

class JsonTweetDeserializer extends KafkaJsonDeserializerTemplate[Tweet] {
  override def clazz: Class[Tweet] = classOf[Tweet]
}
