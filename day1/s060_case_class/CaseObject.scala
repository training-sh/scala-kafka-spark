package workshop.s060_case_class

// Singleton objects, useful for enumerations/types

sealed trait Topping
case object Cheese extends Topping
case object Pepperoni extends Topping
case object Sausage extends Topping
case object Mushrooms extends Topping
case object Onions extends Topping

sealed trait CrustSize
case object SmallCrustSize extends CrustSize
case object MediumCrustSize extends CrustSize
case object LargeCrustSize extends CrustSize

sealed trait CrustType
case object RegularCrustType extends CrustType
case object ThinCrustType extends CrustType
case object ThickCrustType extends CrustType

case class Pizza (
                   crustSize: CrustSize,
                   crustType: CrustType,
                   toppings: Seq[Topping]
)

// For Akka messages types, discussed later
case class StartSpeakingMessage(textToSpeak: String)
case object StopSpeakingMessage
case object PauseSpeakingMessage
case object ResumeSpeakingMessage

object CaseObject extends  App {
  val pizza1 = Pizza(SmallCrustSize, RegularCrustType, List(Sausage,Cheese))
  println("Pizza1 ", pizza1);
}
