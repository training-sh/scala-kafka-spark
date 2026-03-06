package day2

object S20_PatternMatching extends  App {

  // match and case expression, that returns results (similar to switch case statement in java)

  val num = 4
  val result =  num match {
    case 1 => "One"
    case 2 => "Two"
    case _ => "SomethingElse" // default
  }

  println(result) // 2 match, print Two
  println(result) // 1 match, print One
  println(result) // 4 match, print SomethingElse


 // match multiple values in case branch, use pipe sign
  val result2 =  num match {
    case 1  | 3 | 5 | 7 | 9=> "Odd"
    case 2 | 4 | 6 | 8 => "Even"
    case _ => "Unknown" // default
  }

  println(result2)

  // Match data type, case classes

  val value: Any =  true // 100.0 // 10  // "Scala" // Int, "Scala" string

  val result3 = value match {
    case i: Int => i * 10
    case s: String => "Hello " + s
    case d: Double => d * .9
    case _ => ()
  }

  println(result3)

  // case with if guard
  val price = 30 // 100, 55, 30

  val discount = price match {
    case p: Int if p < 50 => 0 // no discount
    case p: Int if p >= 50 && p < 100 => 5 //5 % discount
    case _ => 10 // 10%
  }

  println("discount", discount)


  // case classes

  case class Notebook(name: String, price: Int)
  case class Mobile(name: String, brand: String, price: Int)
  case class Car(name: String, price: Int, year: Int)

  // val product: Any = Notebook("80 pages", 50)
  //val product: Any = Mobile("iphone", "Apple", 50000)
  //val product: Any = Mobile("galaxy", "sam", 15000)
  val product: Any = Car("Swift",  1000000, 2026)

  val result4 = product match {
    case n: Notebook => 0 // 0 tax
    case m: Mobile if m.price >= 20000 => 18
    case m: Mobile if m.price < 20000 => 5
    // destructing, unapply function
    // Car(name, price, year) but we ignore name and year, pick only price
    case Car(_,p, _)  =>  if (p >= 1000000) 12 else 5
    case _ => 18
  }

  println("tax ", result4)

}
