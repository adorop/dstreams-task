package com.aliaksei.darapiyevich.dstreams.task.functions

import java.util.{Collections, Date}

import com.aliaksei.darapiyevich.dstreams.task.UnitTestSpec
import com.aliaksei.darapiyevich.dstreams.task.model.{HashtagEntity, Place, Tweet}

class CheckMandatoryPropertiesTest extends UnitTestSpec {
  private val createdAt = new Date
  private val hashtags = Collections.singletonList(new HashtagEntity())
  val placeWithoutCountryCode = new Place()
  val placeWithCountryCode = new Place("country code")

  private val checkMandatoryProperties = new CheckMandatoryProperties

  "check" should "return false when place.countryCode is not defined" in {
    val result = checkMandatoryProperties(new Tweet(createdAt, placeWithoutCountryCode, hashtags))
    result shouldBe false
  }

  it should "return false when hashtags are empty" in {
    val result = checkMandatoryProperties(new Tweet(createdAt, placeWithCountryCode, Collections.emptyList()))
    result shouldBe false
  }

  it should "return true when countryCode and hashtags are defined" in {
    val result = checkMandatoryProperties(new Tweet(createdAt, placeWithCountryCode, hashtags))
    result shouldBe true
  }
}
