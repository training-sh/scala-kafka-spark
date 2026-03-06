package day2

object S13_HigherOrderFunction2 extends  App {
  // sumOfSomething
  // higher order functions are part of framework
  // func arg are developer logic
  def sumOfSomething(f: (Int) => Int, numbers:Int*): Int = {
    var s = 0

    for (i <- numbers) {
      s += f (i) // f is an abstraction, based on developer code
    }

    s //return
  }

 // developer business logic
  val id = (n: Int) => n
  val sq = (n: Int) => n * n
  val cube = (n: Int) => n * n * n

  val r1 = sumOfSomething(id , 1, 2, 3) // 1 + 2 + 3 = 6
  val r2 = sumOfSomething(sq , 1, 2, 3) // 1 * 1 + 2 * 2 + 3 * 3 = 14
  val r3= sumOfSomething(cube , 1, 2, 3) // 1 * 1 * 1 + 2 * 2 * 2 + 3 * 3 * 3 = 36

  println(r1, r2, r3)

  // DIY
  // Reimplement sumOfSomething as sumOfSomethingFunctionN, but use FunctionN, don't use def
  //Reimplement sumOfSomething using function signature (Int => Int), Int*
}
