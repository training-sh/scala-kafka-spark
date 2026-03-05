package day1

object S06_Classes extends  App {

  // class definition

  class Product {
    // class constructor , similar to default constructor in java
    println("inside Product class constructor") // example

    // member variables, public is default scope
    // public, private, protected
    // public - accessible everywhere
    // private - inside the class
    // protected - for sub classes

    // public scope
    def show() = {
      println("Show func")
      println("Name ", name)
      println("Price", price)
    }


    // using string template s"", in python, f string, javascript `${}'
    def show2() = {
      println(s"Name: $name")
      println(s"Price: $price")
    }

    def show3() = {
      // multi line string, plus format,
      println(
        s"""
          |Name: $name
          |Price: $price
          |""".stripMargin)
    }

    // member variables, also values
    // public scope

    // var name: String = ""
    // var price:Int = 0

    // private scope
    private var name: String = ""
    private var price:Int = 0

    // write GETTER and SETTER in JAVA style
    def getName = name
    def setName(v: String)  = name = v  // Assign v to name

    // Write GETTER SETTER using SCALA STYLE
    private var _discount = 0 // Int

    // let us write scala style getter setter
    def discount = _discount  // GETTER

    // NO SPACE BEFORE _= , we try to write = operator function
    // discount_= is a method name
    // (d: Int) is func arg
    // _discount = d is function body
    // _discount is a private variable
    def discount_= (d: Int) = _discount = d

  }


  // create instance 1
  val p1 = new Product // this calls class constructor, execute code in the class constructor body
  // class constructor called for every instance
  // instance 2
  val p2 = new Product // this calls class constructor, execute code in the class constructor body

  p1.setName("iPhone")

  p1.show   // function call
  p1.show() // function call, use this

  // println("P1 name", p1.name), name is in private scope


  println(p1.getName)
  p1.show()

  p1.show2()

  p1.show3()

  p1.discount =  10 // calls setter discount_= function, 10 is passed as d
  println ("discount ", p1.discount) // calls getter discount
}
