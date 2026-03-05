package workshop.s042_class_inheritance

object ClassTypes extends App {

  class Animal(val name:String) {
    override def toString: String = s"$name"
  }

  class Cat (name:String) extends Animal(name) {

  }

  class Dog (name: String) extends  Animal(name) {
      def speak () = {
        println("Bow")
      }

      def walk = {
        println("Walk")
      }
  }

  def printAll(animals : Animal*): Unit = {
    animals.foreach(println);
    animals.foreach( a => println(a.getClass));
  }

  printAll(new Animal("My Pets"), new Dog("My Dog"), new Cat("My Cat"))

  val dog = new Dog("My Dog 2")
  val dogClass = classOf[Dog]

  println(dogClass.getMethods)

  println(if (classOf[Dog] == dog.getClass) "Dog Only" else "May be cat")

  println("Is dog class", dog.isInstanceOf[Dog])

  println("Is dog class", dog.isInstanceOf[Animal])

}
