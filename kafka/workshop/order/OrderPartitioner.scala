package kafka.workshop.order

import org.apache.kafka.clients.producer.Partitioner
import org.apache.kafka.common.{Cluster, PartitionInfo}

import java.util.{List, Map, Random}
// import scala.jdk.CollectionConverters._

class OrderPartitioner extends Partitioner {

  private val random = new Random()

  println("OrderPartitioner created")

  // called when producer starts
  override def configure(props: Map[String, _]): Unit = {
    // configuration if needed
  }

  // called by producer.send()
  override def partition(
                          topic: String,
                          key: Any,
                          keyBytes: Array[Byte],
                          value: Any,
                          valueBytes: Array[Byte],
                          cluster: Cluster
                        ): Int = {

    var partition = 0

    println("Partition Thread ID " + Thread.currentThread().getId)

    // get partitions for topic
    val partitions: List[PartitionInfo] = cluster.partitionsForTopic(topic)

    val numPartitions = partitions.size()

    println("Total partitions " + numPartitions + " for topic")

    if (numPartitions <= 1) {
      return 0
    }

    // key-based routing
    val country = key.asInstanceOf[String]

    if (country == "IN") {
      partition = 0
    } else if (country == "USA") {
      partition = 1
    } else {
      partition = 2
    }

    // value-based routing
    val order = value.asInstanceOf[Order]

    if (order.amount > 0 && order.amount <= 1000) {
      partition = 0
    }

    if (order.amount > 1000 && order.amount <= 10000) {
      partition = 1
    }

    if (order.amount > 10000) {
      partition = 2
    }

    println(s"For key $key Part $partition")

    partition
  }

  override def close(): Unit = {
    // cleanup if needed
  }
}