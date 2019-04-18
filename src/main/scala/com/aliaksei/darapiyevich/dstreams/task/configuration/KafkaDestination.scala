package com.aliaksei.darapiyevich.dstreams.task.configuration

case class KafkaDestination(
                             bootstrapServer: String,
                             topic: String,
                             options: Map[String, String]
                           ) {

}
