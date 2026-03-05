package day1

object S09_CompanionObject extends  App {
  // In the same scala file, package, if we have a class and object with same name,
  // it called as companion object

  // They both must be in same file/package ,not in differnt file, not in differnt packages

  // CLASS
  class Brand(val name: String, val id: Int) {
    println("Brand created", name, id)
  }

  //COMPANION OBJECT
  object Brand { // name is same as Class name
    println("Brand object constructor") // must be called once

    // Help you create object as factory,
    def apply(name: String, id: Int) = {
      println("Apply called ")
      new Brand(name, id) // return new object
    }

    def apply(name: String): Brand = apply(name, 0) // calls apply(name: String, id: Int)

    // unapply func, destructing data from instance
    def unapply(brand: Brand): Option[String] = {
      println("unapply called")
      Some(brand.name)
    }


  }

  // to create object, we need new keyword, normal case
  // val b1 = new Brand("Apple", 1)

  // create object using companion object
  // calls Brand object apply function, notice, no new keyword
  val b2 = Brand("Samsung", 2)

  val b3 = Brand.apply("Moto", 3)

  val b4 = Brand("Google") // calls apply with 1 parameter def apply(name: String)

  // destructuring / Data Extraction from the object
  // typically without unapply func
  val name = b3.name
  val id = b3.id

  // Using unapply to destruct the values
  val Brand(n) = b3 // extract b3.name, assign to n, as good calling Brand.unapply(b3)
  println("n ", n)

}
