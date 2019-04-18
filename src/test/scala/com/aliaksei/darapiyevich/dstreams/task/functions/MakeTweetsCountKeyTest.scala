package com.aliaksei.darapiyevich.dstreams.task.functions

import java.time.LocalDateTime

import com.aliaksei.darapiyevich.dstreams.task.UnitTestSpec
import com.aliaksei.darapiyevich.dstreams.task.model.FlattenHashtagTweet

class MakeTweetsCountKeyTest extends UnitTestSpec {
  private val year = 2000
  private val month = 1
  private val dayOfMonth = 2
  private val hour = 12
  private val createAt = LocalDateTime.of(year, month, dayOfMonth, hour, 1, 1)
  private val countryCode = "countryCode"
  private val hashTagInCamelCase = "hashTagCamelCase"
  private val tweet = new FlattenHashtagTweet(createAt, countryCode, hashTagInCamelCase)

  private val makeTweetsCountKey = new MakeTweetsCountKey()

  "result.hour" should "be concatenated year date and hour" in {
    val result = makeTweetsCountKey(tweet)
    result.getHour shouldEqual s"$year$month$dayOfMonth$hour"
  }

  "result.hashtag" should "be lowercased" in {
    val result = makeTweetsCountKey(tweet)
    result.getHashtag shouldEqual hashTagInCamelCase.toLowerCase()
  }

}
