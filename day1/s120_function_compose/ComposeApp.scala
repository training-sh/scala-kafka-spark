package workshop.s120_function_compose

object ComposeApp extends  App {
  val totalCost: Double = 10

  val applyDiscountValFunction = (amount: Double) => {
    println("Apply discount function")
    val discount = 2 // fetch discount from database
    amount - discount
  }

  val amountBeforeTax = applyDiscountValFunction(totalCost)

  val applyTaxValFunction = (amount: Double) => {
    println("Apply tax function")
    val tax = 1 // fetch tax from database
    amount + tax
  }

  val amountAfterTax = (applyDiscountValFunction compose applyTaxValFunction) (totalCost);

  println("Total Cost ", totalCost);

  println("amountBeforeTax ", amountBeforeTax);

  println("amountAfterTax ", amountAfterTax);

  // And then Example
  // f(x) andThen g(x) = g(f(x)).
  // The results of the first function f(x) is ran first and will be passed as input to the second function g(x)

//
//  val amountAfterTaxAnThen = (applyDiscountValFunction andThen applyTaxValFunction) (totalCost);
//
//  println("amountAfterTaxAnThen ", amountAfterTax);
//

}
