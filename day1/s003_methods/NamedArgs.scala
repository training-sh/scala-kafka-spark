package workshop.s003_methods

object NamedArgs extends  App {
  def diff(a: Int, b: Int) : Int = a - b

  println(diff(10, 20)); // normal way, a = 10, b = 20

  // named argument
  println(diff(b= 20, a= 10)) // a = 10, b = 20
}
