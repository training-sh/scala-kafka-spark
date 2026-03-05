package workshop.s006_conditions

object If {

  def ifExample() : Any = {
    if (true) 10
  }


  def ifElseExample() : Any = {
    if (false) 10 else 20
  }


  def ifExpr() : Any = {
    val result = if (false) 10 else 20
    println(result);
  }

  def ifElseIfExpr(a: Int, b : Int, c: Boolean) : Any = {
     if (a > b)
       if (c)
          100
       else
          50
     else 10
  }


  def ifElseIfExprBlock(a: Int, b : Int, c: Boolean) : Any = {
    if (a > b) {
      if (c) {
        100
      }
      else {
        50
      }
    }
    else {
      10
    }
  }

  def main(args: Array[String]): Unit = {
    println("ifExample ", ifExample)
    println("ifElseExample ", ifElseExample)
    ifExpr();
    println (ifElseIfExpr(10, 5, true))
    println (ifElseIfExpr(10, 5, false))
    println (ifElseIfExpr(5, 15, false))


    println (ifElseIfExprBlock(10, 5, true))
    println (ifElseIfExprBlock(10, 5, false))
    println (ifElseIfExprBlock(5, 15, false))

  }
}
