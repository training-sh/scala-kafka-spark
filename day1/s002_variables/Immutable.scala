package workshop.s002_variables

object Immutable extends  App {
  val PI: Double = 3.14;
  val PI_Float: Float = 3.14f;
  val name = "Scala";
  val version = 2.13;
  val year: Long = 2020;
  // error reassignment to val
  // year = 2021
  println("PI " + PI);


}
