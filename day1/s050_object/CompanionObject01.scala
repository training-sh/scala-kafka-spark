package workshop.s050_object

class Brand(val name: String, val id: Int =0) {
}

// when class name and object are same,
// companion object
object Brand {
  // object factory
  def apply(n: String) = new Brand(n)
  def apply() = new Brand("")

  // extraction
  def unapply(brand: Brand) : Option[String] = {
    println("unapply called");
    Some(brand.name)
    // returns None
    // when id not present None
  }

  //  val Fan(id) = fan1;  // unapply
}

object Test2 {
  def apply() = println("Hello world")
}


object CompanionObject01  extends  App {
  val b1 = new Brand("Google");

  // sugar, it calls Brand.apply
  val b2 = Brand("Microsoft");
  println(b2.name)

  // calls unapply method, take tuple, extract name
  val Brand(name) = b2;
  println("Name is ", name);

  val b3 = Brand() // default apply

  Test2(); //Test2.apply
}
