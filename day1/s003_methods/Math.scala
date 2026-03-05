package workshop.s003_methods

// Scala has two conventions
// 1. Method [Similar to Java member functions]
// 2. Function [Similar to Java Lambda -> ]

// Detailed discussion later

object Math  extends  App {

  def add(a: Int, b: Int): Int = a + b
  // Implicit Type Int assumed
  def sub(a: Int, b:Int ) = a - b;
  // Block, with implicit Int
  // Block, doesn't need return statement
  // the last executed expression value returned
  def mul(a: Int, b:Int): Int = {
    a * b
  }

  // using return statement
  // explicit return type MUST
  def div (a: Int, b: Int): Int = {
    return a / b
  }

  // a little factorial
  def fact(n:Int): Int =
  {
    if(n == 1) 1 // returned
    else n * fact(n - 1) // returned
  }

  // GCD - Greatest common divisor
  def gcd(x:Int, y:Int): Int=
  {
    if (y == 0) x
    else gcd(y, x % y)
  }

  println("Add " + add(20 ,10));
  println("Add " + sub(20 ,10));
  println("Add " + mul(20 ,10));
  println("Add " + div(20 ,10));
  println("fact " + fact(5));
  println ("GCD " + gcd(25, 15));
}
