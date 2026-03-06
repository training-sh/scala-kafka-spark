package workshop.s035_either

object EitherExample extends  App {
  def safeDiv(a: Int, b: Int): Either[String, Int] =
    if (b != 0) Right(a / b) else Left("Divide by zero!")

  val x = safeDiv(10, 2)
  println("X ", x.right, x.isRight, x.isLeft);

  // safe way to access
  if (x.isRight) {
    println("result value ", x.right.get)
  }

  val y = safeDiv(10, 0)
  println("Y ", y.left, y.isRight, y.isLeft);

  if (y.isLeft) {
    println("error code ", y.left.get)
  }
}
