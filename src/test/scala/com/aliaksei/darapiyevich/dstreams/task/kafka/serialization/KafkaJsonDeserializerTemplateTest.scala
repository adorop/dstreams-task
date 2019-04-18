package com.aliaksei.darapiyevich.dstreams.task.kafka.serialization

import com.aliaksei.darapiyevich.dstreams.task.UnitTestSpec
import com.aliaksei.darapiyevich.dstreams.task.model.Tweet
import org.apache.kafka.common.serialization.Deserializer
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse
import org.scalatest.BeforeAndAfterAll

import scala.io.Source

class KafkaJsonDeserializerTemplateTest extends UnitTestSpec with BeforeAndAfterAll {
  implicit val formats: DefaultFormats.type = DefaultFormats
  private val topic = "topic"

  private val testTweetLocation = getClass.getResource("/tweet.json").toURI
  private val testTweet = parse(Source.fromFile(testTweetLocation).mkString)

  private var result: Tweet = _

  override protected def beforeAll(): Unit = {
    val deserializerClass = classOf[TweetKafkaJsonDeserializer].getName
    val deserializer = Class.forName(deserializerClass)
      .newInstance()
      .asInstanceOf[Deserializer[Tweet]]
    result = deserializer.deserialize(topic, Source.fromURI(testTweetLocation).mkString.getBytes)
  }


  "deserialze" should "parse createdAt to Date" in new {
    result.getCreatedAt.getTime shouldBe expected_createdAt
  }

  private def expected_createdAt: Long = {
    (testTweet \ "createdAt").extract[BigInt].longValue()
  }

  "deserialze" should "parse place" in new {
    result.getPlace.getCountryCode shouldEqual (testTweet \ "place" \ "countryCode").extract[String]
  }

}

class TweetKafkaJsonDeserializer extends KafkaJsonDeserializerTemplate[Tweet] {
  override def clazz: Class[Tweet] = classOf[Tweet]
}