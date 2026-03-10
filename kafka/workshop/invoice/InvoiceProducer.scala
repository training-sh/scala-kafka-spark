package kafka.workshop.invoice

import kafka.workshop.Settings
import kafka.workshop.models.Invoice

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import java.util.{Properties, UUID, Random}


// kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3 --topic invoices


object InvoiceProducer {

  val TOPIC = "invoices"

  val random = new Random()

  val categories = Array(1, 2, 3, 4)
  val customerIds = Array(1000, 2000, 3000, 4000, 5000, 6000)
  val customerNames = Array("Krish", "Gayathri", "Nila", "Venkat", "Hari", "Ravi")
  val stateIds = Array("KA", "TN", "KL", "MH", "DL", "AP")

  def getNextRandomInvoice(): Invoice = {

    val stateId = stateIds(random.nextInt(stateIds.length))
    val customerId = UUID.randomUUID().toString
    val id = UUID.randomUUID().toString

    val invoice = new Invoice()

    invoice.setId(id)
    invoice.setCustomerId(customerId)
    invoice.setQty(random.nextInt(5) + 1)
    invoice.setAmount(random.nextInt(5000) + 100)
    invoice.setCountry("IN")

      import java.time.Instant
      invoice.setInvoiceDate(Instant.now())


      //invoice.setInvoiceDate(Instant.ofEpochMilli(System.currentTimeMillis()))

      invoice.setState(stateId)

    invoice
  }

  def main(args: Array[String]): Unit = {

    val events = 100000L

    val props = new Properties()

    props.put("bootstrap.servers", Settings.BOOTSTRAP_SERVERS)
    props.put("acks", "all")
    props.put("retries", "0")

    props.put("key.serializer",
      "org.apache.kafka.common.serialization.StringSerializer")

    props.put("value.serializer",
      "io.confluent.kafka.serializers.KafkaAvroSerializer")

    props.put("schema.registry.url", Settings.SCHEMA_REGISTRY)

    val producer = new KafkaProducer[CharSequence, Invoice](props)

    for (nEvents <- 0L until events) {

      val invoice = getNextRandomInvoice()

      val key = invoice.getState

      val record =
        new ProducerRecord[CharSequence, Invoice](TOPIC, key, invoice)

      producer.send(record).get()

      println(s"Sent Invoice $invoice")

      Thread.sleep(5000)
    }

    producer.close()
  }
}