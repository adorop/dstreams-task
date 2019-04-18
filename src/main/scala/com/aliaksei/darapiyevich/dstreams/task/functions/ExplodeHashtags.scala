package com.aliaksei.darapiyevich.dstreams.task.functions

import com.aliaksei.darapiyevich.dstreams.task.functions.utils._
import com.aliaksei.darapiyevich.dstreams.task.model.{FlattenHashtagTweet, Tweet}

import scala.collection.JavaConverters._

class ExplodeHashtags extends (Tweet => TraversableOnce[FlattenHashtagTweet]) with Serializable {

  override def apply(tweet: Tweet): TraversableOnce[FlattenHashtagTweet] = {
    tweet.getHashtagEntities.asScala
      .map(hashtag => new FlattenHashtagTweet(tweet.getCreatedAt, tweet.getPlace.getCountryCode, hashtag.getText))
  }
}
