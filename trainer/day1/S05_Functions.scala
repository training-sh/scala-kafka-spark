package day1

// Functions in scala, lambda in Java/Python, Arrow function in JavaScript
// Annonymous function, without name
// Functions are object, first class citizen
// Functions can be returned from another function, functions can be passed as argument
// and functions can be assigned as refernce to variable..
// In java/python always one line, but scala you can go multi line
object S05_Functions extends  App {
  // => Arrow or lambda, internally, Scala function mapped into Java Lamdas

  val pow = (n: Int) => n * n

  println (pow(5)) // 25
  println (pow.getClass) // day1.S05_Functions$$$Lambda$6/123961122

  val p = pow // assigng to another variable, reference assignment, both p and pow refers to same func
  println (p(10)) // 100

  // =>

  // val lambda_exp = (variable:Type) => Transformation_Expression

  // lambda expression/ aka function in Scala
  // returns types are implicits
  val add = (a: Int, b: Int )  => a + b
  val sub = (a: Int, b: Int ) => {
    a - b
  }

  println ( add(20, 10) )
  println ( sub(20, 10) )
}
