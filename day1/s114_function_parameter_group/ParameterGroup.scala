package workshop.s114_function_parameter_group

object ParameterGroup extends  App {

  def add(a: Int, b: Int, c: Int) = a + b + c
  def sum(a: Int)(b: Int)(c: Int) = a + b + c

  def printDetail(firstName: String,
            lastName: String)(age: Int) = s"$firstName $lastName aged $age"


  println(add(10,20,30))

  println(sum(10)(20)(30))

  println(printDetail("Nila", "Krish")(8))

  val mm =  printDetail _
  var k = mm ("Nila", "Krish")
  var r = k(10)
  println("Result ", r)

  println("RR ", k {50})

  def ifBothTrue(f:  => Boolean) (g:  => Boolean) (codeBlock: => Unit) =
    if (f  && g ) {
      codeBlock
    }




  val age = 20
  val numAccidents = 0


  ifBothTrue(age > 18)(numAccidents == 0) {
    println("Discount!")
  }


  def whilst(testCondition: => Boolean)(codeBlock: => Unit) {
    while (testCondition) {
      codeBlock
    }
  }

  var i = 0
  whilst (i < 5) {
    println(i)
    i += 1
  }

}
