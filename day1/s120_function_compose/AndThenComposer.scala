package workshop.s120_function_compose

// , f(x) andThen g(x) = g(f(x)).
// The results of the first function f(x) is ran first and will be passed as input to the second function g(x).
object  AndThenComposer extends App {
  val totalCost: Double = 10

  val applyDiscountValFunction = (amount: Double) => {
    println("Apply discount function")
    val discount = 2 // fetch discount from database
    amount - discount
  }

  val discountValue = applyDiscountValFunction(totalCost);

  val applyTaxValFunction = (amount: Double) => {
    println("Apply tax function")
    val tax = 1 // fetch tax from database
    amount + tax
  }

  val result =  (applyDiscountValFunction andThen applyTaxValFunction)(totalCost);
  println("Result ", result);


}
