package kafka.workshop.order

import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.errors.SerializationException

import java.nio.charset.StandardCharsets
import java.util.Map

// Convert JSON bytes -> Order object
class OrderDeserializer extends Deserializer[Order] {

  println("OrderDeserializer created")

  override def configure(props: Map[String, _], isKey: Boolean): Unit = {
    println("Deserializer configured with props: " + props)
  }

  override def deserialize(topic: String, bytes: Array[Byte]): Order = {

    println("OrderDeserializer deserialize called")

    if (bytes == null)
      return null

    try {

      val json = new String(bytes, StandardCharsets.UTF_8).trim

      println("Received JSON = " + json)

      // use Order companion object
      val order = Order.fromJson(json)

      order

    } catch {
      case e: Exception =>
        println("Error while parsing JSON")
        throw new SerializationException("Error deserializing Order", e)
    }

  }

  override def close(): Unit = {
    // nothing to clean
  }

}