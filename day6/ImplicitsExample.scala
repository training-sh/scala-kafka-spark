package workshop.section21_implicits

//https://stackoverflow.com/questions/5598085/where-does-scala-look-for-implicits/5598107

// stack overflow
object ImplicitsExample extends App {

  // probably in a library
  class Prefixer(val prefix: String)
  def addPrefix(s: String)(implicit p: Prefixer) = p.prefix + s

  // then probably in your application
  implicit val myImplicitPrefixer = new Prefixer("***")
   println(addPrefix("abc"))  // returns "***abc"


  implicit def doubleToInt(d: Double) = d.toInt
  val x: Int = 42.0

  //Works as
  //def doubleToInt(d: Double) = d.toInt
  //val x: Int = doubleToInt(42.0)

}

object ImplicitsExample2 extends  App {

    implicit val i : Int = 1                        //> i  : Int = 1

    val ii : Int = 2

    def add(a:Int)(implicit b:Int) = a + b          //> add: (a: Int)(implicit b: Int)Int

    println(add(10)(15)  )                                   //> res0: Int = 25
    println(add(10))                                      //> res1: Int = 11


}
