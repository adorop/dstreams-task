package com.aliaksei.darapiyevich.dstreams.task.functions

import java.util
import java.util.Date

import com.aliaksei.darapiyevich.dstreams.task.UnitTestSpec
import com.aliaksei.darapiyevich.dstreams.task.model.{FlattenHashtagTweet, HashtagEntity, Place, Tweet}

class ExplodeHashtagsTest extends UnitTestSpec {
  private val createdAt = new Date()
  private val countryCode = "country code"

  private val firstHashtag = "firstHashtag"
  private val secondHashtag = "secondHashtag"

  private val tweet = new Tweet(
    createdAt,
    new Place(countryCode),
    util.Arrays.asList(new HashtagEntity(firstHashtag), new HashtagEntity(secondHashtag))
  )

  private val explodeHashtags = new ExplodeHashtags()

  "explode hashtags" should "return FlattenHashtagTweet on each hashtag entity" in {
    val result = explodeHashtags(tweet)
    result.map(_.getHashtag).toList shouldEqual Seq(firstHashtag, secondHashtag)
  }

  "explode hashtags" should "return FlattenHashTweets with properties from Tweet" in {
    val result = explodeHashtags(tweet)
    verifyHasCountryAndCreatedAtFromTweet(result)
  }

  private def verifyHasCountryAndCreatedAtFromTweet(result: TraversableOnce[FlattenHashtagTweet]): Unit = {
    result.foreach { flattenHashtagTweet =>
      flattenHashtagTweet.getCountryCode shouldBe countryCode
      flattenHashtagTweet.getCreatedAt shouldEqual utils.date2LocalDateTime(createdAt)
    }
  }
}
