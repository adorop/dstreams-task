package com.aliaksei.darapiyevich.dstreams.task.configuration

case class ApplicationConfiguration(
                                     kafkaSource: KafkaSource,
                                     kafkaDestination: KafkaDestination,
                                     checkpointDirectory: String,
                                     microBatchDurationMillis: Long
                                   ) {

}
