package com.aliaksei.darapiyevich.dstreams.task.configuration

case class KafkaSource(
                        bootstrapServer: String,
                        consumerGroupId: String,
                        topic: String,
                        autoOffsetReset: String = "latest"
                      ) {

}
