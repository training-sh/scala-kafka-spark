package kafka.workshop.order

import kafka.workshop.Settings
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}
import org.apache.kafka.clients.producer.ProducerConfig._

import java.nio.charset.StandardCharsets
import java.util.Properties
import scala.util.Random


// kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3 --topic orders
// kafka-console-consumer --bootstrap-server localhost:9092 --topic orders  --from-beginning --property print.key=true --property print.timestamp=true


object OrderProducer {

  val TOPIC = "orders"

  val countries = Array("IN", "USA", "EU", "AU", "DE")

  val r = new Random()

  def nextOrder(): Order = {

    val amount = 100.0 + r.nextInt(1000)
    val orderId = r.nextInt(1000000).toString
    val customerId = r.nextInt(100).toString
    val country = countries(r.nextInt(countries.length))

    Order(orderId, amount, customerId, country)
  }

  def main(args: Array[String]): Unit = {

    println("Welcome to producer")

    val props = new Properties()

    props.put(BOOTSTRAP_SERVERS_CONFIG, Settings.BOOTSTRAP_SERVERS)
    props.put(ACKS_CONFIG, "all")
    props.put(RETRIES_CONFIG, "0")
    props.put(BATCH_SIZE_CONFIG, "16000")
    props.put(LINGER_MS_CONFIG, "100")
    props.put(BUFFER_MEMORY_CONFIG, "33554432")


    props.put(
      KEY_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer"
    )

    props.put(
      VALUE_SERIALIZER_CLASS_CONFIG,
      classOf[OrderSerializer].getName
    )

    props.put(
      "partitioner.class",
      classOf[OrderPartitioner].getName
    )

    // Producer<String, Order>
    val producer: Producer[String, Order] = new KafkaProducer[String, Order](props)

    for (i <- 0 until 10) {

      val order = nextOrder()

      val key = order.country

      val record = new ProducerRecord[String, Order](TOPIC, key, order)
      // add header
      record.headers().add(
        "Content-Type",
        "application/json".getBytes(StandardCharsets.UTF_8)
      )

      println("Sending " + order.orderId)

      // internally calls serializer + partitioner
      producer.send(record)

      println(s"order sent $record")

      Thread.sleep(5000)
    }

    producer.close()

  }

}