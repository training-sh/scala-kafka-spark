package workshop.s036_try

import scala.util.{Try, Success, Failure} // it's scala.util.Try

object TrySuccessExample  extends  App {

  def safeDiv(a: Int, b: Int): Try[Int] =
    Try(a / b)

  val x = safeDiv(10, 2)

  if (x.isSuccess)
    println("we got result ", x.get)


  //x: scala.util.Try[Int] = Success(5)
  println(x);

  val y = safeDiv(10, 0)
  //y: scala.util.Try[Int] = Failure(java.lang.ArithmeticException: / by zero)
  println(y);

  if (y.isFailure) {
    println("Failed ", y.failed.get)

    // re throw exception

    // throw y.failed.get

    //println("we got failure ", y.failed) // failed has exception object
  }
}
