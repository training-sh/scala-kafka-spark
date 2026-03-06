package day2

object S18_Either extends  App {
  // get user by user id,
  //    outputs
  //           output 1, user found, RIGHT for Valid cases
  //           output 2, table not found, LEFT ("table not found")
  //           output 2, db host nnot reasble, LEFT ("Connection error")
  //           output 3, if rest api, LEFT (404, 500)

  // Either [Left Type, Right Type]
  val e1: Either[Int, String  ] = Right("{age: 10}")
  val error: Either[Int, String] = Left(500) // Internal server eror

  // we have right vlaue
  println("e1", e1.isLeft, e1.isRight) // false, true

  // we have left value
  println("eror", error.isLeft, error.isRight) // true, false

  if (e1.isRight) println("e1 right ", e1.right)
  if (error.isLeft) println("error left ", error.left)

  // DIY: 3-4 mins, ues for comprehension over either both e1 and error

  // resolve the right
  for { r <- e1} println("For comp on right ", r)

  // error is left, below will not print
  for { r2 <- error} println("For comp on left, will not print ", r2)


  def safeDiv(a: Int, b: Int): Either[String, Int] =
    if (b != 0) Right(a/b) else Left("Divide by Zero")

  val result = safeDiv(10, 2)
  println(result) // Right(5)

  val result2 = safeDiv(10, 0)
  println(result2) // Left(Divide by Zero)

  val result4 = for {
       x <- safeDiv(10, 2) // 5
       y <- safeDiv(9, 3) // 3
       z <- safeDiv(8 , 4) // 2
       } yield x * y * z  //5 * 3 * 2 = 30 Right(30)

  println("result4", result4)


  val resultError = for {
    x <- safeDiv(10, 2) // 5
    y <- safeDiv(9, 3) // 3  // try one of the divisor is 0
    z <- safeDiv(8 , 0) // Left(Divide by Zero)
  } yield x * y * z   // will not evaluate, as z is LEFT, does not hold result

  println("resultError", resultError)
}
