package day2

object S15_Closure extends  App {


  // Closure is a functional state, have a data inside a function, even after func executed/exited
  // Curry function, that returns another function as return value

  def not_closure (seed: Int, step: Int) = {
    var count = seed

    count += step


    count
  }

  println (not_closure(100, 1)) // 101, count inside not_closure shall be gone after exiting

  // return Func0[Int] ----> () => Int
  // called as curry function
  def seq (seed: Int, step: Int): () => Int = {
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

    incr // return a function as output, curry function
  }

  // incr0By1 refer to incr function inside seq
  // since we have ref to incr in incr0By1, count shall be in memory
  val incr0By1 = seq(0, 1) // 0 is start, 1 is step

  println(incr0By1()) // calls incr(), return 0
  // count is state, protected inside the closure, encapsulation
  println(incr0By1()) // calls incr(), return 1
  println(incr0By1()) // calls incr(), return 2
  println(incr0By1()) // calls incr(), return 3

  // each seq has its own state/memory, does not interfere incr0By1 state

  def test () = {
    val I = 10
    val incr1000By10 = seq(1000, 10)
    println(incr1000By10()) // 1000
    println(incr1000By10()) // 1010
    println(incr1000By10()) // 1020
  } // incr1000By10 is out of scope, does incr1000By10 has any visiblity outside?

  test() // here incr1000By10 is out of scope? state, count will be released after GC

  {
    val incr100By10 = seq(100, 10)
    println(incr100By10()) // 100
    println(incr100By10()) // 110
    println(incr100By10()) // 120
  }
  // incr100By10 out of scope?? Garbage Collector try to remove incr100By10 reference
  // not count state inside seq will be removed for incr100By10

  // incr0By1 has independent state
  println(incr0By1()) // 4

  // incr0By1 state count will be there until program completion




}
