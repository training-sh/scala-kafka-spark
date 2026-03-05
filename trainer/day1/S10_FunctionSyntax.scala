package day1

object S10_FunctionSyntax extends  App {
  // Def are methods, not objects
  // functions are objects

  // implicit return
  val add = (a: Int, b: Int) => a + b

  // declare a function with a data type
  // what is declartion
  // Int is data type
  val i : Int = 10 // i is value, Int is data type

  // (Int, Int) => Int  is  function type, that reprent a function, its input, output
  // (Int, Int) => Int  is a function type declartion
  // (a, b) => a - b is function implementation
  // (a, b) are two parameters
  // => a - b is function body
   // = is assignment
  val sub: (Int, Int) => Int = (a, b) => a - b

  // write a power function, pass n, return n * n , using this function declartion
  // 5 mins

  val power: (Int) => Int = (n) => n * n

  val pow: Int => Int = n => n * n // we don't need parenthese for 1 arg

  // functionN - N is number of parameters, a function always return value, 1 value
  // Function1[Int, Int], Function1 means 1 input, 1 output
  // Function1[Int, Int], first Int is input data, LAST ONE ALWAYS RETURN TYPE

  val powN: Function1[Int, Int] = n => n * n // we don't need parenthese for 1 arg

  val powN1: (Int) => Int = powN // assining function PowN to powN1 variable

  println(power(5))

  println(powN(10))

  println(powN1(10))
}
