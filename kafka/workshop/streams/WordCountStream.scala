package kafka.workshop.streams

import java.util.{Arrays, Properties}

import kafka.workshop.Settings
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}
import org.apache.kafka.streams.kstream.{KStream, KTable, Materialized, Printed, Produced}
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.state.KeyValueStore
object WordCountStream {

  def main(args: Array[String]): Unit = {

    val TOPIC = "sentence"

    val props = new Properties()

    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-count-stream-feb2022")
    props.put(StreamsConfig.CLIENT_ID_CONFIG, "word-count-stream-client")
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Settings.BOOTSTRAP_SERVERS)

    props.put(
      StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
      classOf[Serdes.StringSerde]
    )

    props.put(
      StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
      classOf[Serdes.StringSerde]
    )

    props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, "1000")
    props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0")

    val builder = new StreamsBuilder()

    // Read from Kafka
    val wordStream: KStream[String, String] =
      builder.stream[String, String](TOPIC)

    // Print incoming records
    wordStream.print(Printed.toSysOut())

    // Split words
    val words: KStream[String, String] =
      wordStream
        .flatMapValues(v => Arrays.asList(v.toLowerCase.split("\\W+"): _*))

    words.print(Printed.toSysOut())

    // Word count
    val counts: KTable[String, java.lang.Long] =
      words
        .groupBy((_, word) => word)
        .count(Materialized.as[String, java.lang.Long, KeyValueStore[Bytes, Array[Byte]]]("wordCount"))

    val wordCountStream =
      counts.toStream()

    // Print results
    wordCountStream.print(Printed.toSysOut())

    // Publish to Kafka
    wordCountStream.to(
      "word-count",
      Produced.`with`(Serdes.String(), Serdes.Long())
    )

    val streams = new KafkaStreams(builder.build(), props)

    streams.start()

    sys.addShutdownHook {
      streams.close()
    }
  }
}