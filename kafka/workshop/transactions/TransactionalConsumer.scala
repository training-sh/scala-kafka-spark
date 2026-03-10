package kafka.workshop.transactions


import kafka.workshop.Settings
import org.apache.kafka.clients.consumer.{KafkaConsumer, ConsumerRecord}
import org.apache.kafka.clients.consumer.ConsumerConfig._

import java.time.Duration
import java.util.{Collections, Properties}

/*



 */
object TransactionalConsumer {

  val TOPIC = "tx-demo"

  def main(args: Array[String]): Unit = {

    val props = new Properties()

    props.put(BOOTSTRAP_SERVERS_CONFIG, Settings.BOOTSTRAP_SERVERS)

    props.put(GROUP_ID_CONFIG, "tx-demo-consumer")

    props.put(KEY_DESERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringDeserializer")

    props.put(VALUE_DESERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringDeserializer")

    props.put(AUTO_OFFSET_RESET_CONFIG, "earliest")

    // important we have two options , read_committed, read_uncommitted
    props.put(ISOLATION_LEVEL_CONFIG, "read_committed")

    val consumer = new KafkaConsumer[String,String](props)

    consumer.subscribe(Collections.singletonList(TOPIC))

    println("Consumer started")

    while(true) {

      val records = consumer.poll(Duration.ofSeconds(1))

      records.forEach { record: ConsumerRecord[String,String] =>

        println(
          s"offset=${record.offset()} key=${record.key()} value=${record.value()}"
        )

      }

    }

  }

}