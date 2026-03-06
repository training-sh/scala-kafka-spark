package day2

object  S17_Options extends  App {
  // Option - alternative

  // To handle the errors? we throw exceptions
  // Exceptions will break the complete call stack
  /*
  call func 1
      open rest api
     funct 2
        open file
        funct 3..
         open db
          function 4
                throw exception

   */

  // In functional programming, exceptions are considered bad
  // we have to handle exception similar to return values
  // div(x): 42/x, will result or exception if x is 0
  // read(filepath):  will get file content else may see exceptions (file not found,permission)
  // sql(query): will get result, or error/exception..

  // Option => (Some, None)
  // Fetch ticket -> result is either get a Some ticket or None

  // without option

  def parseInt(input: String): Int = {
    input.toInt
  }

  val r = parseInt("100") // return 100
  println(r)

 // val r2 = parseInt("#NaN") // throw exception , halt the program

  // Option , parseInt, either get a parsed value, or get None, don't throw exception

  val o1: Option[Int] = Some(10) // has value
  val o2: Option[Int] = None // no value

  println("o1", o1.isDefined, o1) // defined is true
  println("o2", o2.isDefined, o2) // defined is false

  if (o1.isDefined)
      println("Option o1 has value", o1.get) // get will get the int value ie 10

  if (o2.isEmpty) println("o2 is empty", o2)

  // how to simplify this access, using for comprehention

  println("===================")
  // n <- o1, check for isDefined, if it is defined, then only it get the value, then for body will be executed
  for { n <- o1 } {
    // n is Int, not option
    println("Value of o1 in for comprehension", n)
  }

  // For for None option
  for { n <- o2 } {
    // will not execute this block, as o2 is None
    println("this will not print")
  }


  // now let us use Option in code, to shield exceptions

  def toInt(input: String): Option[Int] = {
    // try catch is an expression, that return value
    val result = try {
      Some(input.toInt)
    } catch {
      case _ => None
    }

    println("Option toInt ", result)
    result // return result which is Option
  }


  def toInt2(input: String): Option[Int] = {
    // try catch is an expression, that return value
    try {
      Some(input.toInt)
    } catch {
      case _ => None
    }
  }

  val r4 = toInt("100") // Some(100)
  val r5 = toInt("#Nan") // None , does not throw exception

  println("r4", r4)
  println("r5", r5)

  //TODO: 2 mins, use for comprehension, get value 100 from r4, multiply the value by 10
  for { n <- r4 } println(n * 10)

  println("Program completed")


}
