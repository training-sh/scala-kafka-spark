package workshop.s105_function_n

object FunctionN extends  App {
  val fun0 = new Function0[String] {
          def apply(): String = sys.props("java.version")
        }

  val power =  new Function1[Int, Int] {
    def apply(n: Int) = n * n;
  }

  val add =  new Function2[Int, Int, Int] {
    def apply(a: Int, b: Int) = a + b;
  }

  val sub = (a: Int, b: Int) => a - b;
  val mul: (Int, Int) => Int  =  (a, b) => a * b;

  println(fun0())
  println(power(5))
  println(add(10, 20))
  println(sub(10, 20))
  println(mul(3, 5))
}
