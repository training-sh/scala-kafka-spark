package workshop.section08_patterns

object ListExtraction extends  App {

  // Reverse with Recursive
  def reverse[T](xs: List[T]): List[T] = xs match {
    case List() => List()
    case x :: xs1 => {
      println("X is ", x, "Xs is ", xs1)
      reverse(xs1) ::: List(x)
    }
  }

  println(reverse(List( "Name", "Is", "Scala")))

}
