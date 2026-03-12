package workshop.section21_implicits

object Low {
  def low = "object Low"
  def shoot = "missed!"
}

object High {
  def high = "object High"
  def shoot = "bulls eye!"
}

trait LowPriority {
  implicit def intToLow(a: Int): Low.type = Low
}

object HighPriority extends LowPriority {
  implicit def intToHigh(a: Int): High.type = High
}

object ImplicitPriority extends App {

  import HighPriority._


  val a: Int = 1
  print((a.low, a.high, a.shoot)) // (object Low,object High, bulls eye!)
}