package day1

object S04_Methods extends  App {

  // Copy sum func code here
  // As good like java class function, not lambda
  // has name

  // a and b are args, ): Int is return type, a + b is function body , no need for return statement
  def sum(a: Int = 0, b: Int = 0): Int = a + b;

  println(sum()); // a =0, b = 0
  println(sum(10)); // a = 10 , b = 0
  println(sum(10, 20)); // a = 10 , b = 20

  // copy maths code in github


  // explit : Int used for return type
  def add(a: Int, b: Int): Int = a + b

  // Implicit Type Int assumed
  // :Int after ): is shown shadow in Intellji. it is intellji text
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

  // if - in many languages if is a statement, does not return value
  // scala if - expression, returns value, similar to ternary operator

  val r1 = if (10 % 2 == 0) "Even" else  "Odd";  // if is expression, return either even or odd
  println(r1)

  val r2 = if (10 % 2 == 0)
            "Even"
          else
            "Odd"

  println(r2)

  // block, enclosed by { }, contains expressions, last executed result shall be returned

  val k = {
    10 + 20
    56 - 3
    42
    1 + 2 // this expression shall be evaluated, last expression result shall be assinged into k
  }

  println ("K ", k, k.getClass) // K 3, Int

  // if else with block
  val r3 = if (10 % 2 == 0) {
    println("If truthy even")
    "Even" // "Even" is return value, last evaluated output, returend
  } else {
    println ("if falsy odd")
    "Even"
  }

  println(r3) // Even


  // Copy from named args
  def diff(a: Int, b: Int) : Int = a - b

  println(diff(10, 20)); // normal way, a = 10, b = 20

  // named argument
  println(diff(b= 20, a= 10)) // a = 10, b = 20

  // Variable number of args

  // * represent variable number of args
  def printAll(names: String*)  {
    names.foreach(println);
  }

  def printNumbers(numbers: Int*): Unit = {
    numbers.foreach(println)
  }

  def sum(numbers:Int*):Int={
    var s=0
    for(n <- numbers){
      s+=n
    }
    s // return the total
  }

  printAll(); //  print nothing, no args
  printAll("Hello");
  printAll("Welcome", "to", "Scala")
  printNumbers(1, 2, 3)

  println(sum(1, 2,3)); // 6

  // List to splat/spread, making an List to spread

  // only works with * arguments
  val fruits = List("Apple", "Orange", "Mango")
  printAll(fruits: _*) // convert the collection into arguments
  printNumbers(List(100, 200, 300): _*)  // convert the collection into arguments

}
