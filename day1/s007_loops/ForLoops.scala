package workshop.s007_loops

object ForLoops {
  /*
    For Loops

   */

  def forIterator () : Unit = {
    println("For List Iterator")
    val list = List(10, 20, 30, 40, 50);
    for (i <- list)
        println(i);
  }

  def forWithTo(): Unit = {
    println("for loop using to");
    for (i <- 1 to 5)
      println(i)
  }


  def forWithUntil(): Unit = {
    println("for loop using until");
    // 1 to 4
    for (i <- 1 until 5)
      println(i)
  }

  def forWithRange(): Unit = {
    println("for using range ");
    for (i <- Range(10, 15))
        println(i)
  }


  // If in the for is called guard
  def forWithIfGuard(): Unit = {

    println("for using if even number  ");
    for (i <- 1.to(10) if i % 2 == 0)
      println(i)
  }

  def main(args: Array[String]) = {
     forIterator()
    forWithTo()
    forWithUntil()
    forWithRange()
    forWithIfGuard();
  }


}
