package day1

/*
multi line
*/
// Comments
object S00_Main {

  // Write code in object body
  println("Object body ") // executed before main function
  // Object body is a constructor , executed before the first use of the object

  // def function e(args): return type = {function body}
  def main (args: Array[String]) : Unit = {
    // variable/value name: data type = assignment
    // Unit is a return type, tehcnically called empty tuple, similar to  void
    println("Hello")
    // Run: Right click, Run S00_Main


  }
}
