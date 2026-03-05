package workshop.s101_functions_syntax

object BasicFunctions extends App {

  // Implicit returns
  val add = (a: Int, b: Int) => a + b

  // Function with explicit return type, tricky syntax  for first timer
  val sub: (Int, Int) => Int = (a, b) => a - b

  // sub is a operand/a variable/ function reference
  // (Int, Int) => Int is a type of a function that takes two int inputs, return int output

  // = is assignment

  // (a, b) => a - b is a function body, that accept two int params and return 1 int params

  println(add(20, 10));

  println(sub(20, 10));




}
