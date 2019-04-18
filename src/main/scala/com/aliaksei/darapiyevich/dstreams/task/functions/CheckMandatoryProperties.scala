package com.aliaksei.darapiyevich.dstreams.task.functions

import com.aliaksei.darapiyevich.dstreams.task.model.Tweet

class CheckMandatoryProperties extends (Tweet => Boolean) with Serializable {


  override def apply(tweet: Tweet): Boolean = {
    countryCodeIsDefined(tweet) && hashtagsAreDefined(tweet)
  }

  private def countryCodeIsDefined(tweet: Tweet): Boolean = {
    Option(tweet.getPlace)
      .flatMap(place => Option(place.getCountryCode))
      .isDefined
  }

  private def hashtagsAreDefined(tweet: Tweet): Boolean = {
    val hashtags = tweet.getHashtagEntities
    hashtags != null && !hashtags.isEmpty
  }
}
