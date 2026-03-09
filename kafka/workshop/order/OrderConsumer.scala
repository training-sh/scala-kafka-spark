package kafka.workshop.order

import kafka.workshop.Settings
import org.apache.kafka.clients.consumer.{ConsumerRecord, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.clients.consumer.ConsumerConfig._

import java.time.Duration
import java.util.{Collections, Properties}

object OrderConsumer {

  val TOPIC = "orders"

  def main(args: Array[String]): Unit = {

    val props = new Properties()

    props.put(BOOTSTRAP_SERVERS_CONFIG, Settings.BOOTSTRAP_SERVERS)

    props.put(GROUP_ID_CONFIG, "order-consumer-group")

    props.put(ENABLE_AUTO_COMMIT_CONFIG, "false")
    props.put(AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")

    props.put(SESSION_TIMEOUT_MS_CONFIG, "30000")

    props.put(
      KEY_DESERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringDeserializer"
    )

    props.put(
      VALUE_DESERIALIZER_CLASS_CONFIG,
      classOf[OrderDeserializer].getName
    )

    // KafkaConsumer<String, Order>
    val consumer = new KafkaConsumer[String, Order](props)

    // subscribe to topic
    consumer.subscribe(Collections.singletonList(TOPIC))

    println("Consumer Starting!")

    while (true) {

      val records: ConsumerRecords[String, Order] =
        consumer.poll(Duration.ofSeconds(1))

      if (records.count() > 0) {

        val iterator = records.iterator()

        while (iterator.hasNext) {

          val record: ConsumerRecord[String, Order] = iterator.next()

          println(
            s"partition=${record.partition()}, offset=${record.offset()}, key=${record.key()}, value=${record.value()}"
          )

          val order = record.value()

          println("order no " + order.orderId)

          val header = record.headers().lastHeader("Content-Type")

          if (header != null) {
            val value = new String(header.value())
            println("Header Content-Type = " + value)
          }
        }

        consumer.commitSync()
      }

    }
  }
}