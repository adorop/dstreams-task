package com.aliaksei.darapiyevich.dstreams.task.watermark

import java.time.temporal.ChronoUnit
import java.time.{Duration, LocalDateTime}

import com.aliaksei.darapiyevich.dstreams.task.watermark.UpdateRunningCountTransformation._
import com.aliaksei.darapiyevich.dstreams.task.{CompletedBatchesLatch, DStreamsIntegrationTestSpec}
import org.apache.spark.rdd.RDD

import scala.collection.mutable

class UpdateRunningCountTransformationTest extends DStreamsIntegrationTestSpec {
  private val batchesQueue = new mutable.Queue[RDD[(String, LocalDateTime)]]()
  private val latch = new CompletedBatchesLatch(2)

  it("should output only relevant records") {
    val actualTwoTimesKey = "actual 2 times"
    val actualSecondTimeKey = "actual 1 time"
    val lateKey = "late"

    val expected = List(
      (actualTwoTimesKey, 1),
      (actualTwoTimesKey, 2),
      (actualSecondTimeKey, 1)
    )

    val window = ChronoUnit.HOURS
    val watermark = Duration.of(1, ChronoUnit.MINUTES)

    val late = LocalDateTime.now
      .minus(1, window)
      .minus(watermark)
    val actual = LocalDateTime.now

    addBatchToQueue(
      Seq(
        (actualTwoTimesKey, actual),
        (actualSecondTimeKey, late),
        (lateKey, late)
      )
    )

    val result = mutable.ArrayBuffer[(String, Long)]()

    streamingContext.queueStream(batchesQueue)
      .withinWindow(window, watermark)
      .on(key = _._1, eventTime = _._2)
      .count()
      .foreachRDD(result ++= _.collect())


    streamingContext.addStreamingListener(latch)

    streamingContext.start()
    addBatchToQueue(
      Seq(
        (actualTwoTimesKey, actual),
        (actualSecondTimeKey, actual),
        (lateKey, late)
      )
    )
    latch.await()
    streamingContext.stop()

    result.toList.sorted shouldEqual expected.sorted
  }

  def addBatchToQueue(seq: Seq[(String, LocalDateTime)]): Unit = {
    batchesQueue += sparkContext.parallelize(seq)
  }

  override def durationMillis: Long = 100
}