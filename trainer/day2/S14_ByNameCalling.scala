package day2

object S14_ByNameCalling extends  App {

  // Myfunc return hello

  // doSomething( myFunc() ) , what argument doSomething will get?
  // doSomething will get String, value is hello

  def doSomething(input: String) = {
    println("Dosomething called")
    println("input", input)
  }

  def MyFunc()  = {
    println("MyFunc called")
    "Hello"
  }

  // Not a higher order funcitom, output MyFunc pass as input to doSomething
  doSomething( MyFunc()  )
  // MyFunc called
  // Dosomething called
  // input Hello

  // NOW BEGIN By Name Calling
  // Block { } or expression without brace
  // Block, there is no input arg, correct or not?

  /*
  expression, single line or multi line {}, return output, does output has data type or not?
  Output data type, there is no input
  {
    10
    20
    10 + 5
  }
  */

  // No input for this block, but there is output
  val a: Int = {
    println("inside block")
    2 + 3
  }
  println(a) // 5

  // How do you syntatically define this block in code
  //  => Int   (this has no input, but there is an output)

  println("-=========================================")
  def nano() ={
    println("Getting Nano Time ")
    System.nanoTime
  }

  // differed execution
  def delayed (t: => Long) = {
    println("Inside delayed")
  }

  def delayed2 (t: => Long) = {
    println("Inside delayed2")
    t // will execute the code passed as arg, this calls nano func

  }


  delayed(  nano() )
  // Call delayed function first, Inside Delayed, does not call nano() here
  // t block is not used anywre inside delayed

  // delayed2 is called, with nano() code block
  delayed2 ( nano() )
  // inside delayed 2
  // Getting Nano Time

  // t: => Long
  delayed ( {
    println("Delayed block, will not print")
    42L
  })

  delayed2 ( {
    println("Delayed2 block, will  print")
    42L
  })
}
