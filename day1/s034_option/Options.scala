package workshop.s034_option

object Options extends  App {

  // returns Option[Int] or Option[None]
  def toInt(input: String): Option[Int] = {
    try {
      Some(input.toInt)
    } catch {
      case _ => None
    }
  }

  def parseInt(input: String): Int = {
    try {
      input.toInt
    } catch {
      case t: Throwable => throw t
    }
  }


  // Option[1000]
  val result = toInt("1000");

  println("Is empty ", result.isEmpty);
  println("Is defined ", result.isDefined);
  println("value ", result.get)

  val failedResult = toInt("wrong-data")
  println("Is empty ", failedResult.isEmpty);
  // println("get ", failedResult.get) // throws exception
  if (failedResult.isDefined)
    println(failedResult.get)
  else println("No value ")

  val dataPoints = List("10", "20", "30", "nan", "missing", "", "40");

  // List [Option[Int]]
  val results = dataPoints.map(toInt)
    .filter( option => option.isDefined)
    .map (option => option.get) // List[Int]


  println(results.min)
  println(results.max)
  println(results.sum)

}
