package workshop.s109_function_closure

// Closure functions define a bound scope for variable
// the variable live even the function execution completed
object Closure extends  App {
  // seq function accept two arguments, seed and step
  // the method bound, seed, step and count every time seq called
  // seed, step and count are internal state
  def seq (seed: Int, step: Int = 1) : () => Int = {
    var count = seed

    var incr = () => {
      val current = count
      count += step
      count
    }

    incr
  }

  val seq1 = seq(0);
  val seq10 = seq(0, 10);
  println("1 series ", seq1());
  println("1 series ", seq1());
  println("10 series ", seq10());
  println("10 series ", seq10());
  println("1 series ", seq1());
  println("10 series ", seq10());

}
