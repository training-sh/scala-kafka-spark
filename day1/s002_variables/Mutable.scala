package workshop.s002_variables

object Mutable extends  App {
  // var means variables, allows to change value
  var name = "Scala"
  name = "Dotty Compiler"

  var version = 2.11;
  version = 2.12;
  version = 2.13

  println(name)
  println(version)
}
