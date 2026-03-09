package kafka.workshop

package kafka.workshop

// SimpleConsumer.java

import org.apache.kafka.clients.consumer.{ConsumerRecord, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.PartitionInfo

import java.time.Duration
import java.util.{Collections, List, Properties, UUID}

import org.apache.kafka.clients.consumer.ConsumerConfig._

object SimpleConsumer {

  val TOPIC = "greetings"

  def main(args: Array[String]): Unit = {

    val props = new Properties()

    props.put(BOOTSTRAP_SERVERS_CONFIG, Settings.BOOTSTRAP_SERVERS)

    // Consumer group id, should be unique, partition allocated to consumers inside teh group
    props.put(GROUP_ID_CONFIG, "greetings-consumer-group") // offset, etc, TODO

    // __consumer_offset is topic with 50 partitions used to track offset processed by kafka consumers
    // once read the message from offset, consumer should commit the offset back to broker on topic __consumer_offset

    // -- true, automatically commit the offset back to broker as soon message is received
    // RISK: consumer read the message, may be not processed, but still chances that consumer commit to broker stating message processed
    // -- false, developers manually commit the offset
    props.put(ENABLE_AUTO_COMMIT_CONFIG, "false")
    props.put(AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000") // commit every 1 second

    props.put(SESSION_TIMEOUT_MS_CONFIG, "30000")

    // key deserialize the bytes data into String format, JSON,AVRO formats
    props.put(KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")

    // value deserialize the bytes data into String format, JSON,AVRO formats
    props.put(VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")

    // to read all data always
    //props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
    // props.put(ConsumerConfig.CLIENT_ID_CONFIG, "your_client_id");
    // From beginning
    //props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    // <Key as string, Value as string>
    val consumer = new KafkaConsumer[String, String](props)

    // subscribe from one or more topics
    consumer.subscribe(Collections.singletonList(TOPIC))

    println("Consumer starting..")

    val partitions: List[PartitionInfo] = consumer.partitionsFor(TOPIC)
    for (partitionInfo <- partitions.toArray.map(_.asInstanceOf[PartitionInfo])) {

      println("Partition " + partitionInfo)
      println("Leader " + partitionInfo.leader())
    }

    println("---------------------------")

    while (true) {

      // Consumer poll for the data with wait time
      // poll for msgs for 1 second, any messges within second, group together
      // if no msg, exit in 1 second, records length is 0
      val records: ConsumerRecords[String, String] = consumer.poll(Duration.ofSeconds(1))


      // Iterate record and print the record
      val iterator = records.iterator()
      while (iterator.hasNext) {

        val record: ConsumerRecord[String, String] = iterator.next()

        printf("partition=%d, offset=%d\n",
          Int.box(record.partition()),
          Long.box(record.offset()))

        printf("key=%s,value=%s\n", record.key(), record.value())

        // manual commit if ENABLE_AUTO_COMMIT_CONFIG is "false"
        // technically consumer send a message to broker about commited offset against consumer group __consumer_groups
        // when we restart the consumer, it can read from where it left

        consumer.commitSync()

        Thread.sleep(1000)
      }

      // Thread.sleep(2000);
    }
  }
}