package workshop.s037_for_comprehension

object ForComprehension extends  App {

  case class User(id: Int);

  def getUser(id: Int): Option[User] = {
    Some(User(id))
  }

  def isEligible(user: User): Option[Boolean] = {

    println("isEligible called", user)
    // None
    Some(true)
  }

  def getTotal(): Option[Int] = {
    println("getTotal called")
    Some(1000)
  }

  def getDiscount(): Option[Int] = {
    println("getDiscount called")
    Some(20)
  }

  // Check with error.

  val result = for {
    user <- getUser(1)
    eligible <- isEligible(user)
    total <- getTotal()
    discount <- getDiscount()
  } yield  total - (total * discount / 100.0)

  //  println("isEligible", isEligible())
  //  println("getDiscount", getDiscount())
  //
  println("End Result ", result)

  def toInt(input: String): Option[Int] = {
    try {
      Some(input.toInt)
    } catch {
      case _ => None
    }
  }

  val dataPoints = List("10", "20", "30", "nan", "missing", "", "40");
  // FIXME:
  val resultSet = for { item <- dataPoints.map(toInt).flatten }
    yield item;

  println("Result set ", resultSet)

  val resultSet2 = for { item <- dataPoints.flatMap(toInt) }
    yield item;

  println("resultSet2  ", resultSet2)

}
