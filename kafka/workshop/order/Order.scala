package kafka.workshop.order

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

case class Order(
                  orderId: String,
                  amount: Double,
                  customerId: String,
                  country: String
                ) {

  // serialize object to JSON
  def toJSON(): String = {
    Order.objectMapper.writeValueAsString(this)
  }

}

object Order {

  private val objectMapper = new ObjectMapper()

  // IMPORTANT: register Scala support
  objectMapper.registerModule(DefaultScalaModule)

  // deserialize JSON string -> Order
  def fromJson(json: String): Order = {
    objectMapper.readValue(json, classOf[Order])
  }

}