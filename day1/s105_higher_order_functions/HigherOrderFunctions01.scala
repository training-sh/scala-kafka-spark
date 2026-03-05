package workshop.s105_higher_order_functions

object HigherOrderFunctions01 extends  App {

  val doWhatISay:  (Int => Int, Int) => Int  = (f, a) => {
    f (a)
  }


  val id = (n: Int) => n

  val sq = (n: Int) => n * n

  val cube = (n: Int) => n * n * n

  println("Identity", doWhatISay(id, 100))
  println("SQ", doWhatISay(sq, 2))
  println("Cube", doWhatISay(cube, 3))

}
