package kafka.workshop.invoice

import kafka.workshop.Settings
import kafka.workshop.models.Invoice

import org.apache.kafka.clients.consumer.{ConsumerRecord, ConsumerRecords, KafkaConsumer}
import java.util.Properties
import java.time.Duration
import java.util.Collections

import org.apache.kafka.clients.consumer.ConsumerConfig._
import scala.collection.JavaConverters._

object InvoiceConsumer {

  val TOPIC = "invoices"

  def main(args: Array[String]): Unit = {

    val props = new Properties()

    props.put(BOOTSTRAP_SERVERS_CONFIG, Settings.BOOTSTRAP_SERVERS)
    props.put(GROUP_ID_CONFIG, "invoice-consumer-example")

    props.put(ENABLE_AUTO_COMMIT_CONFIG, "true")
    props.put(AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")
    props.put(SESSION_TIMEOUT_MS_CONFIG, "30000")

    props.put(KEY_DESERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringDeserializer")

    props.put(VALUE_DESERIALIZER_CLASS_CONFIG,
      "io.confluent.kafka.serializers.KafkaAvroDeserializer")

    props.put("schema.registry.url", Settings.SCHEMA_REGISTRY)

    // important as we need Specific, instead of Generic Record

    props.put("specific.avro.reader", "true")


    val consumer = new KafkaConsumer[String, Invoice](props)

    consumer.subscribe(Collections.singletonList(TOPIC))

    println("Consumer Starting!")

    while (true) {

      val records: ConsumerRecords[String, Invoice] =
        consumer.poll(Duration.ofSeconds(1))

      if (records.count() == 0)
        ()

      for (record <- records.iterator().asScala) {

        println(
          s"partition=${record.partition()}, offset=${record.offset()}, key=${record.key()}, value=${record.value()}"
        )

        //this code needs props.put("specific.avro.reader", "true")
        val invoice: Invoice = record.value()

        println(s"Invoice ID ${invoice.getId}")

      }
    }
  }
}