package com.aliaksei.darapiyevich.dstreams.task

import java.util.concurrent.CountDownLatch

import org.apache.spark.streaming.scheduler.{StreamingListener, StreamingListenerBatchCompleted}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Duration, StreamingContext}
import org.scalatest.{FunSpec, Matchers}

trait DStreamsIntegrationTestSpec extends FunSpec with Matchers {
  private val defaultCheckpoint = "/tmp/streaming"

  private lazy val config = new SparkConf()
    .setAppName(getClass.getSimpleName)
    .setMaster("local[2]")
    .set("spark.ui.enabled", "false")
    .set("spark.default.parallelism", "1")

  lazy val streamingContext: StreamingContext = new StreamingContext(config, Duration(durationMillis))
  streamingContext.checkpoint(System.getProperty("test.checkpoint.dir", defaultCheckpoint))

  lazy val sparkContext: SparkContext = streamingContext.sparkContext

  def durationMillis: Long
}

class CompletedBatchesLatch(batchesToWait: Int) extends StreamingListener {
  private val latch: CountDownLatch = new CountDownLatch(batchesToWait)

  override def onBatchCompleted(batchCompleted: StreamingListenerBatchCompleted): Unit = {
    latch.countDown()
  }

  def await(): Unit = {
    latch.await()
  }
}