package workshop.s060_case_class

// A case class is a class with immutable members
// with Companion object
case class Product(name: String, price: Int);
case class Brand(id: String, name: String);

object CaseClasses extends  App {
   val p1 = Product("iPhone", 40000);
  println("Product 1" + p1);
}
