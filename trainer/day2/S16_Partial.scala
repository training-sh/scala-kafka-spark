package day2

object S16_Partial extends  App {
  def not_partial_div(x: Int) = {
    println("not_partial_div called", x)
   42 / x
  }

  //not_partial_div(2) // 21
 //  not_partial_div(0) // exception,we need to guard against input parameter which cannot be satisfied
  //readCsv ("filepath not exists/not permiteed")

  // if input is wrong, don't even call the function
  // PartitialFunction is based on a guard function/logic, decide whether a function can be executed
  // or not based on predicate


  val divide = new PartialFunction [Int, Int] {
    // Logic of the function, apply is called only if isDefinedAt return true
    // apply not called for 0 during list collect
    def apply(x: Int) = {
      println("apply", x)
      42/ x
    }

    // Guard/predicate, you may notice, 0 is passed here during List::collect
    def isDefinedAt(x: Int) = {
      println("isDefinedAt", x)
      x != 0
    }
  }

  println(divide.isDefinedAt(2)) // true
  println(divide.isDefinedAt(0)) // false

  // divide(0) // calls divide.apply, does not automatically calls isDefinedAt

  if (divide.isDefinedAt(0)) divide(0) // does not call divide function
  if (divide.isDefinedAt(2)) divide(2) //   call divide function

  println ("LIST..===================")
  val numbers = List(2, 4,  6)
  println("numbers", numbers)

  val r1 = numbers.collect(divide) // 42 / 2, 42 / 4, 42 / 6
  println("r1", r1)

  // 0, shall cause division error?
  val numbers2 = List(2, 4, 0, 6)
  println("numbers2", numbers2)

  // List, collect func respect partitial function, it calls isDefinedAt automatically
  val r2 = numbers2.collect(divide) // 42 / 2, 42 / 4, 42 / 6
  println("r2", r2)

  // but not all the functions respect partial functions
  // map, does not call isDefinedAt
  // numbers2.map (divide) // throw exception



}
