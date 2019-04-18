# Streaming task

* Consume _hashtag_ topic (from Kafka Homework) with Spark Streaming. In one hour window group entities and calculate
 counts by _hashtag_ and _country_
* Due to bans and timeouts, the data can come out of order and with delays, and the ability to revise results as new data arrives is mandatory.
* Store results in another Kafka topic.

## Main packages

* [watermark](src/main/scala/com/aliaksei/darapiyevich/dstreams/task/watermark) implements functionality of grouping
_DStream_ elements within window with watermarking
* [kafka](src/main/scala/com/aliaksei/darapiyevich/dstreams/task/kafka) contains _Kafka_ related classes:
  * [KafkaJsonDeserializerTemplate](src/main/scala/com/aliaksei/darapiyevich/dstreams/task/kafka/serialization/KafkaJsonDeserializerTemplate.scala)
  provides sub-classes with ability to have parameterless constructor and specify _target class_
  * [KafkaSink](src/main/scala/com/aliaksei/darapiyevich/dstreams/task/watermark) is implementation of _saveToKafka_
  functionality on _DStream_
  
## Build

```
$ mvn clean package -Dtest.checkpoint.dir=${sparkCheckpointDirectory}
```
  
_test.checkpoint.dir_ system property is required for _Spark_ integration testing, since it involves aggregations. It's optional on _Linux_ OS and defaults to _/tmp/streaming_

## Run

[Main class](src/main/scala/com/aliaksei/darapiyevich/dstreams/task/Main.scala) requires one command line argument to
be provided: path to json file with a format defined in [ApplicationConfiguration](src/main/scala/com/aliaksei/darapiyevich/dstreams/task/configuration/ApplicationConfiguration.scala).
An [example](run_on_hdp.json) of such file is provided.

```
$  spark-submit \
> --name "DStreamsTask" \
> --master yarn \
> dstreams-task-1.0-SNAPSHOT.jar run_on_hdp.json &> dstreams_task.log
``` 