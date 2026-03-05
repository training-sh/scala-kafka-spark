package workshop.s007_loops

object MonadExampleWithFor {
  /*
  Imagine you have a string name and you want to do a bunch of operations on it
  but you want to program defensively. In Java this would be implemented as
  a number of tiered null checks. Scala's Option Monad allows us to do this
  much more elegantly
  1. Option can be one of two states Some or None. None covers any states that
  are not Some in this case ("" , null, Null, Nothing, None"). In pure math terms
  this is called a Monadic Zero. Operations on Monadic Zero have pretty much
  the same properties as Zero. An operation that includes a Monadic Zero yields
  Monadic Zero. Examples:
  val s:Option[String] = Option(null) ---> None
  s.flatMap(x=> Option(x.trim)) --> None
  Once a step in the Monad encounters a None then all other steps
  result in None.

  This allows us to do many things in sequence without worrying that some step
  will cause a subsequent step to fail. If a step encounters a Monadic Zero then
  all subsequent steps will also result in a monadic zero.
  In the example below we have a String name. name might be(maybe) populated
  when we plug it into the Option Monad. The first step results in a Some(name) or
  None(Option(null) returns None), the next step is either Option(name.trim) or None,
  and so on.

  Note: notice the explicit typing on Option(name) this is so nameOp is treated as
  Option[String] other was the Option(nameOp.trim) would complain in the event
  that Option(name) returned None. Its a little kludge brought to you courtesy
  of java's type system.
  */
  def main(args: Array[String]){
    val name = "   My name is "
    // also run with val name = null

    val upper = for {
      nameOp <- Option(name): Option[String]
      trimmed <- Option(nameOp.trim)
      upper <- Option(trimmed.toUpperCase) if trimmed nonEmpty
    } yield upper

    println("*",upper.getOrElse("empty"),"*")
  }

}