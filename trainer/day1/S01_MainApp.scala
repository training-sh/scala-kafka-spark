package day1

// App is a trait, similar to java /C++ interfaces
// App trait already implement main function
object S01_MainApp  extends  App {
  // Object body/constructor, executed automatically
  // Main function implemented in App trait
  println ("Main via trait")
  println(args.length)

  println(args(0)) // args coming from App trait

}
