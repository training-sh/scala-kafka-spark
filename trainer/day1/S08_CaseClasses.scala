package day1

object S08_CaseClasses extends  App {
  // Case classes, useful for data objects
  // used to represent the data
  // Are name and price are member variable?
  // This is normal class, name, and price are constructor variables, not a member variables
  // need to use val or var to make it member variables
  // here we  CANNOT call p.name, can be used within body of Product class , not outside
  // class Product(name: String, price: Int)

  // CASE CLASS
  // by default, name , price are member variable of type val
  // p.price, p.name will work,
  // CASE CLASSES has companion object, used to create object without new keyword
  case class Product(name: String, price: Int)

  // new keyword is not pure object oriented or functional programming
  val p = new Product("iphone", 1000)
  println(p.price)
  println(p) // has builtin toString

  // create object using companion object, without using new keyword
  // This Product("Samsung", 800) does it look like function call or not? Yes
  //  Product("Samsung", 800) calls a function called apply
  val p2 = Product("Samsung", 800) // this internally calls new Product and create object

  val p3 = Product.apply("Galaxy", 700) // same as Product("Samsung", 800) or new Product("Samsung", 800)

  // IDea of case class to avoid new keyword, act like a object factory

  println(p,   p2, p3)

  // destrucuting using case classes

  val n = p.name
  val pr = p.price


  // we are destrucuting name and price
  // order matters, left to right, the first one is name, second is price
  // it calls unapply function automatically
  val Product(  name, price) = p
  println(" name price", name, price)

  val k = Product.unapply(p) // Tuple
  println(k)




}
