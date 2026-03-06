package day2

import scala.util.{Try, Success, Failure}
// import all using _
import scala.util._ // import all

object S19_Try extends  App {
  // Try Catch, it means,   successful or Exception with stack trace

  val t1: Try[Int] = Try(10) // Result
  val t2: Try[Int] = Failure(new Exception("Something went wrong"))

  println("t1", t1.isSuccess) // true
  println("t2", t2.isSuccess) // false

  if (t1.isSuccess) println(t1.get) // print 10
  if (t2.isFailure) println(t2.failed) // exceptions

  // use it over div
  def safeDiv(a: Int, b: Int): Try[Int] = {
    Try (a / b) // if successful, value will be returned, else Failure will assigned
  }

  val o1 = safeDiv(10, 2)
  println(o1.isSuccess, o1.get) // true, 5

  val o2 = safeDiv(10, 0) // failure
  println(o2.isSuccess, o2.failed) // false, java.lang.ArithmeticException: / by zero

  // use it on for comprehension

  val result4 = for {
    x <- safeDiv(10, 2) // 5
    y <- safeDiv(9, 3) // 3
    z <- safeDiv(8 , 4) // 2
  } yield x * y * z  //5 * 3 * 2 = 30 Success(30)

  println("result4", result4)

  val resultError = for {
    x <- safeDiv(10, 2) // 5
    y <- safeDiv(9, 3) // 3  // try one of the divisor is 0
    z <- safeDiv(8 , 0) // Left(Divide by Zero)
  } yield x * y * z   // will not evaluate, as z is LEFT, does not hold result

  println("resultError", resultError)
}
