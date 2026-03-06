package ex

import org.apache.kafka.clients.consumer._
import java.time.Duration
import java.util.{Collections, Properties}

object KafkaConsumerExample {

  def main(args: Array[String]): Unit = {

    val props = new Properties()

    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "scala-consumer-group")

    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringDeserializer")

    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringDeserializer")

    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    val consumer = new KafkaConsumer[String, String](props)

    consumer.subscribe(Collections.singletonList("test-topic"))
    import org.apache.kafka.common.PartitionInfo

    val topic = "test-topic"
    val partitions: java.util.List[PartitionInfo] = consumer.partitionsFor(topic)
    println(s"Topic metadata for: $topic")

    partitions.forEach { p =>
      println(
        s"""
           |Partition: ${p.partition()}
           |Leader: ${if (p.leader() != null) p.leader().host() else "none"}
           |Replicas: ${p.replicas().map(_.id()).mkString(",")}
           |InSyncReplicas: ${p.inSyncReplicas().map(_.id()).mkString(",")}
           |""".stripMargin
      )
    }

    while (true) {

      val records = consumer.poll(Duration.ofMillis(1000))

      records.forEach { record =>
        println(
          s"Received key=${record.key()} value=${record.value()} offset=${record.offset()}"
        )
      }
    }
  }
}