package com.aliaksei.darapiyevich.dstreams.task.functions

import java.time.LocalDateTime

import com.aliaksei.darapiyevich.dstreams.task.model.{FlattenHashtagTweet, TweetsCountKey}

class MakeTweetsCountKey extends (FlattenHashtagTweet => TweetsCountKey) with Serializable {


  override def apply(tweet: FlattenHashtagTweet): TweetsCountKey = {
    new TweetsCountKey(tweet.getCountryCode, toDisplayHour(tweet.getCreatedAt), tweet.getHashtag.toLowerCase)
  }

  def toDisplayHour(createdAt: LocalDateTime): String = {
    s"${createdAt.getYear}${createdAt.getMonth.getValue}${createdAt.getDayOfMonth}${createdAt.getHour}"
  }
}
