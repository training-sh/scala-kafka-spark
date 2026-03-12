package workshop.section21_implicits

object ImplicitApp extends App {
  class A

  class B(val conversion: String)

  implicit val f1: (A => B) = (_) => new B("a2bVal")
  implicit def f2(a: A): B = new B("a2cDef")


  println((new A()).conversion)
  println((new A()).conversion == "a2bVal")

}


object Explicit {
  class A

  class B(val conversion: String)

  implicit val f1: (A => B) = (_) => new B("a2bVal")
  implicit def f1(a: A): B = new B("a2cDef")

  // does not compile
  //assert(f1(new A()))

  // during typechecking  we adapt the first `f1` type to that
  // of its `apply` method with `inserApply`.
  //
  // Implicit resolution doesn't do this before checking specificity
  // which is inconsistent.
}