package kafka.workshop.streams

import java.time.Duration
import java.util.Properties

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}
import org.apache.kafka.streams.kstream._

import scala.jdk.CollectionConverters._


// kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3 --topic sentence
// kafka-console-producer --bootstrap-server localhost:9092  --topic sentence

// kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic word-count-1minute

// kafka-console-consumer --bootstrap-server localhost:9092  --topic word-count-1minute --from-beginning --property print.key=true  --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer

// kafka-topics --list --bootstrap-server localhost:9092

// kafka-topics --describe --topic sentence  --bootstrap-server localhost:9092

object WindowedWordCountStream {
  val TOPIC = "sentence"
  def main(args: Array[String]): Unit = {

    val props = new Properties()
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-count-window-stream")
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, classOf[Serdes.StringSerde])
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, classOf[Serdes.StringSerde])

    val builder = new StreamsBuilder()


    val textLines: KStream[String, String] =
      builder.stream[String, String](TOPIC)

    val words: KStream[String, String] =
      textLines
        .flatMapValues(_.toLowerCase.trim.split("\\W+").toList.asJava)
        .filter((_, word) => word.nonEmpty)

    val wordCounts =
      words
        .groupBy((_, word) => word)
        .windowedBy(TimeWindows.of(Duration.ofMinutes(1)))
        .count()

    import org.apache.kafka.streams.kstream.WindowedSerdes

    wordCounts
      .toStream
      .to(
        "word-count-1minute",
        Produced.`with`(
          WindowedSerdes.timeWindowedSerdeFrom(classOf[String]),
          Serdes.Long()
        )
      )

    val streams = new KafkaStreams(builder.build(), props)
    streams.start()

    sys.addShutdownHook(streams.close())
  }
}