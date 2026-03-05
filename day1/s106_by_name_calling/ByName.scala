package workshop.s106_by_name_calling

/*
  a by name parameter is not evaluated at the point of function application
  but rather it is evaluated at each use within the function
*/
object ByName extends App  {

  def nano() ={
    println("Getting Nano Time ")
    System.nanoTime
  }

  def delayed(t: => Long) = { // indicates by-name parameter
    println("In Delayed Method")
    println("Param: " + t)    //evaluates t
    t                         //Evaluates t again
    //This results in Getting Nano being printed
    //twice
  }


   println(delayed(nano()))



}
