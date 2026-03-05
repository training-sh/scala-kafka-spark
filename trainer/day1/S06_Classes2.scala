package day1

object S06_Classes2 extends  App {
  // name is constructor arg, not a member variable
  // price are public access, value type, need either val or var keyword to make it a member variable

  //class Product(name: String, private var price: Double, protected val discount: Double)

  class Product(name: String,  var price: Double, val discount: Double) {
    // class constructor

  }

  // name, price, discount, are these constructor args or member variables? constructor args
  // name, price, discount can be used inside electrocs class, and pass to base class
  // tax is member variable of Electronics class
  // pass values to base class  Product(name, price, discount)

  class Electronics(name: String, price: Double, discount: Double, val tax: Double)
                extends Product(name, price, discount) {

    def show() = {
      println("Name ", name) // Electronics constructor name
      println("Price ", price) // Electronics constructor price, not member variable this.price
      // this keyword, to refer base class
      // TODO
      // println("Super class price ",  this.super.price) // refer to base class instance variable
    }
  }

  // membervariable, this.name, obj.name, p1.name

  val p1 = new Product("iPhone", 500, 10)
  // println(p1.name) // cannot access name, name is constructor argument, not a member variable
  //println(p1.price)
  println(p1.discount)
 // p1.price = 600 // price is var type
  //p1.discount = 20 // error , discount is val type

  val e1 = new Electronics("Laptop", 1000, 5, 18)
  e1.price = 900
  e1.show()

}
