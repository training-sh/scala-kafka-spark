package workshop.s041_class_access_specifier

class Product {
  // public is default
  // private only this class
  private var name: String = _; // empty string
  private var _price: Int = _; // assign 0
  def price = _price
  def price_= (newPrice:Int) = _price = newPrice

  // this class, child class and this package
  protected  var discount: Int = 0;
}

class Electronics extends  Product {
    def offer(value: Int) = discount = value;
}

object AccessSpecifier extends  App {


}
