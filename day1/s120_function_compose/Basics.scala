package workshop.s120_function_compose

// f(g(x))
// what is executed first?
// g(x) executed first, output is passed to f(x) as input

// composing is a feature, that functions either left to right
// or right to left

// f compose g => f(g(x))
// f andThen g ==> g(f(x))

object ComposerBasics extends  App {
  def f(s: String) = "f(" + s + ")"
  def g(s: String) = "g(" + s + ")"

  val fComposeG = f _ compose g _
  println("fComposeG", fComposeG("yay"))

  val fAndThenG = f _ andThen g _

  println("fAndThenG", fAndThenG("yay"))



}
