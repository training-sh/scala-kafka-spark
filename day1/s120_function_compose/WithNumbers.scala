package workshop.s120_function_compose

object WithNumbers extends  App {
  val add1 = (i: Int) => i + 1
  val double = (i: Int) => i * 2

  val addThenDouble = add1 andThen double

  println("addThenDouble(1) ", addThenDouble(1))
  val doubleThenAdd = add1 compose double

  println("doubleThenAdd(1)", doubleThenAdd(1))
}
