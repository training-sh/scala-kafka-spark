package kafka.workshop.transactions


import kafka.workshop.Settings
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.clients.producer.ProducerConfig._

import java.util.Properties
import scala.util.Random

/*

producer tag the msg with

ProducerId
Epoch - , epoch means version number of a producer session.
Kafka uses version like , 1, 2, 3, 4,..
in general term, epoch means a fixed point in time used as a reference
Sequence number
Transactional flag

--

beginTransaction()
send(msg1)
send(msg2)
send(msg3)
commitTransaction()   OR   abortTransaction()

offset 0  msg1   (transactional)
offset 1  msg2   (transactional)
offset 2  msg3   (transactional)
offset 3  CONTROL_RECORD (COMMIT or ABORT)  ,

CONTROL_BATCH is enum, COMMIT or ABORT are options

The messages themselves are marked transactional.

The last entry is a control record.

Control records are written by the transaction coordinator.

transaction start is known by

ProducerId
ProducerEpoch
SequenceNumber
Transaction flag

only end is marked..

 A special toopic called __transaction_state created internally,
 used by transaction co-oridnator component for tracking transactions


 */

object TransactionalProducer {

  val TOPIC = "tx-demo"

  def main(args: Array[String]): Unit = {

    val props = new Properties()

    props.put(BOOTSTRAP_SERVERS_CONFIG, Settings.BOOTSTRAP_SERVERS)

    props.put(KEY_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")

    props.put(VALUE_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")

    // required for transactions
    props.put(ENABLE_IDEMPOTENCE_CONFIG, "true")
    props.put(TRANSACTIONAL_ID_CONFIG, "tx-producer-1")

    val producer = new KafkaProducer[String,String](props)

    producer.initTransactions()

    val random = new Random()

    for(batch <- 1 to 5) {

      println(s"\n--- Starting transaction batch $batch ---")

      producer.beginTransaction()

      try {

        for(i <- 1 to 5) {

          val msgId = s"$batch-$i"
          val value = s"message-$msgId"

          val record = new ProducerRecord[String,String](
            TOPIC,
            msgId,
            value
          )

          println(s"Sending $msgId")

          producer.send(record)

          // simulate random failure
          if(random.nextInt(10) < 3) {
            throw new RuntimeException("Simulated producer failure")
          }

        }

        println("Committing transaction")

        producer.commitTransaction()

      } catch {

        case e: Exception =>
          println("ERROR occurred → aborting transaction")
          producer.abortTransaction()

      }

    }

    producer.close()
  }

}