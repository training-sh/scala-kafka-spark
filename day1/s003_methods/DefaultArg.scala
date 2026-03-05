package workshop.s003_methods

object DefaultArg extends  App {
  def sum(a: Int = 0, b: Int = 0): Int = a + b;

  println(sum());
  println(sum(10));
  println(sum(10, 20));

}
