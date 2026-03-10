package kafka.workshop

import java.time.Duration
import java.util.{Arrays, Collections, Properties}

import org.apache.kafka.clients.consumer._
import org.apache.kafka.common.{PartitionInfo, TopicPartition}

import scala.collection.JavaConverters._

object RebalanceConsumer {

  val TOPIC = "greetings3"

  def main(args: Array[String]): Unit = {

    val props = new Properties()

    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Settings.BOOTSTRAP_SERVERS)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "rebalance-consumer-66666")
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")
    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000")

    props.put(
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringDeserializer"
    )

    props.put(
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringDeserializer"
    )

    val consumer = new KafkaConsumer[String, String](props)

    // Print partitions
    val partitions = consumer.partitionsFor(TOPIC)

    for (partitionInfo <- partitions.asScala) {
      println(s"Partition $partitionInfo")
    }

    consumer.subscribe(
      Collections.singletonList(TOPIC),
      new ConsumerRebalanceListener {

        override def onPartitionsRevoked(partitions: java.util.Collection[TopicPartition]): Unit = {
          println(s"${partitions.toArray.mkString(",")} topic-partitions are revoked from this consumer")
        }

        override def onPartitionsAssigned(partitions: java.util.Collection[TopicPartition]): Unit = {

          println(s"${partitions.toArray.mkString(",")} topic-partitions are assigned to this consumer")

          val POSITION = "begin" // offset | begin | end | lastCommitted

          for (topicPartition <- partitions.asScala) {

            println(s"Part No ${topicPartition.partition()}")

            println(
              s"Current position is ${consumer.position(topicPartition)} " +
                s"committed is -> ${consumer.committed(topicPartition)}"
            )

            val offsetMeta = consumer.committed(topicPartition)

            POSITION match {

              case "offset" =>
                consumer.seek(topicPartition, 10)

              case "begin" =>
                consumer.seekToBeginning(Arrays.asList(topicPartition))

              case "end" =>
                consumer.seekToEnd(Arrays.asList(topicPartition))

              case "lastCommitted" =>
                if (offsetMeta != null)
                  consumer.seek(topicPartition, offsetMeta.offset())

              case _ =>
            }
          }
        }
      }
    )

    println("Rebalance Consumer Starting!")

    while (true) {

      val records = consumer.poll(Duration.ofSeconds(1))

      if (records.count() == 0)
        ()

      for (record <- records.asScala) {

        println(
          s"partition=${record.partition()}, " +
            s"offset=${record.offset()}, " +
            s"key=${record.key()}, " +
            s"value=${record.value()}"
        )
      }

      consumer.commitSync()
    }
  }
}