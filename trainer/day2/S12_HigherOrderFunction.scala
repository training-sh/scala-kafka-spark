package day2

object S12_HigherOrderFunction extends  App {
  // Higher order function, a function that accept another function as parameter/input
  // reuse business logic at higher level, but lower level implementation may vary

  // val doSomething: (Int => Int, Int) => Int is declartion
  // After the = sign is definition of function
  // doSomething is a higher order function
  val doSomething: (Int => Int, Int) => Int = (fun, n) => {
    println("Do something called ", fun, n)
    fun(n)
  }

  // Function1[Int, Int] equal to Int => Int
 // fun is abstract, we don't know the implementation
  // func is passed as arg to another function
  val doSomething2: Function2[Function1[Int, Int], Int, Int] = (fun, n) => {
    println("Do something called ", fun, n)
    fun(n)
  }


  // define some functions
  // needed for my application, my data processing
  // mph to meters, deg cel to farenheit..
  val id = (n: Int) => n
  val sq = (n: Int) => n * n
  val cube = (n: Int) => n * n * n

  println(doSomething (sq, 5) ) // 25
  println(doSomething (cube, 5) ) // 125
  println(doSomething (id, 5) ) // 5
}
