package day2

object S11_Function_N extends  App{
  // right most is return type, String
  val fun0 = new Function0[String] {
    def apply(): String = sys.props("java.version")
  }

  // right most is return type,
  // 1 arg, which is integer on left
  val power =  new Function1[Int, Int] {
    def apply(n: Int) = n * n;
  }

  val add =  new Function2[Int, Int, Int] {
    def apply(a: Int, b: Int) = a + b;
  }

  // This code is called syntatic sugar
  // scala convert this code into FunctionN syntax
  val sub = (a: Int, b: Int) => a - b;
  val mul: (Int, Int) => Int  =  (a, b) => a * b;

  println(fun0())
  println(power(5))
  println(add(10, 20))
  println(sub(10, 20))
  println(mul(3, 5))
}
