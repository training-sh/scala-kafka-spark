package ex

import org.apache.kafka.clients.producer._
import java.util.Properties

// notepad C:\Windows\System32\drivers\etc\hosts
// 127.0.0.1 broker
object KafkaProducerExample {

  def main(args: Array[String]): Unit = {

    val props = new Properties()

    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)

    val topic = "test-topic"

    for (i <- 1 to 10) {

      val record = new ProducerRecord[String, String](
        topic,
        s"key-$i",
        s"Hello Kafka $i"
      )

      val meta = producer.send(record).get()

      println(s"Sent message $i")
      println("Ack", meta.topic(), meta.offset())
    }

    producer.close()
  }
}