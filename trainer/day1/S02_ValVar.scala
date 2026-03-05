package day1

object S02_ValVar extends  App {
  // mutable data types - variables, the value of the variable can change any time
  // Immutable data types - values are fixed, once defined, we cannot change the values later
  // Scala, gives immutable first priority

  // variable types, mutable values
  var i: Int = 10;
  println(i)
  i += 1 // Mutation
  println(i)
  i = 20 // mutation
  println(i)

  //immutable data types
  val j: Int = 100; //val means immutable, we cannot change value later
  println (j)
  // j += 1   // compiler error
  // j = 200  // compiler error



}
