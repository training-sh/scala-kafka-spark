package kafka.workshop.order

import kafka.workshop.Settings
import org.apache.kafka.clients.producer.ProducerConfig._
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}

import java.nio.charset.StandardCharsets
import java.util.Properties
import scala.util.Random

/*
Problem statement

Producer sends message
Broker receives
ACK lost
Producer thinks message failed
Producer retries <- may cause a duplicate msg in the kafka broker
Without idempotence → duplicate message stored

Order123
Order123   ← duplicate

With idempotent, messages produced with producer id, seq, partition

ProducerID
Sequence Number
Partition

ProducerID  (PID), it is generally a producer instance id

Idempotence does NOT guarantee exactly-once processing.

It only guarantees:

Producer → Kafka log

It does NOT prevent duplicates from:

multiple producers

consumer retries

application bugs
 */
object OrderIdempotentProducer {

  val TOPIC = "orders-4"

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
    // REQUIRED FOR IDEMPOTENCE
    props.put(ENABLE_IDEMPOTENCE_CONFIG, "true")
    props.put(ACKS_CONFIG, "all")
    props.put(RETRIES_CONFIG, Integer.MAX_VALUE.toString)


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