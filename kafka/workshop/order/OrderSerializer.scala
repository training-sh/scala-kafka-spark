package kafka.workshop.order

import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.common.errors.SerializationException

import java.nio.charset.StandardCharsets
import java.util.Map

// Convert Order object -> JSON bytes
class OrderSerializer extends Serializer[Order] {
  println("OrderSerializer object created")

  override def configure(props: Map[String, _], isKey: Boolean): Unit = {
    // no config required
  }

  override def serialize(topic: String, order: Order): Array[Byte] = {

    println("Order serialize called")

    if (order == null)
      return null

    try {

      // use Order class JSON method
      val json = order.toJSON()

      println("JSON text = " + json)

      val bytes = json.getBytes(StandardCharsets.UTF_8)

      bytes

    } catch {
      case e: Exception =>
        throw new SerializationException("Error serializing Order", e)
    }

  }

  override def close(): Unit = {}

}