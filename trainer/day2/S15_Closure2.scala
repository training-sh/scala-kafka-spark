package day2

object S15_Closure2 extends  App {


  def seqWithReset (seed: Int, step: Int): (() => Int, () => Unit) = {
    // count is visible in incr, if someone hold ref of incr, count will be in memory
    // even after exiting seq, count shall be in memory as long incr referenced
    var count = seed // State, hidden inside seq func, not available outside

    var i = 10; // i is not visible inside incr, will be removed after seq
    // Func0
    val incr = () => {
      // count is visible in incr
      // as long as incr referenced somewhere, count shall be in memory, ie state
      val current = count // count is used in another function,
      count += step // increment count

      current // return current value
    }

    val reset = () => count = seed

    (incr, reset) // return a function as output, curry function
  }

  val (incr3333By3, reset3333) = seqWithReset(3333, 3)
  println(incr3333By3()) // 3333
  println(incr3333By3())// 3336
  println(incr3333By3()) // 3339

  println("reset")
  reset3333() // reset back to 3333
  println("after reset")
  println(incr3333By3()) // 3333
  println(incr3333By3())// 3336


}
