package workshop.section25_concurrency

//Mutexes provide ownership semantics. When you enter a mutex, you own it. The most common way of using a mutex in the JVM is by synchronizing on something. In this case, we’ll synchronize on our Person.
//
//In the JVM, you can synchronize on any instance that’s not null.

class Person(var name: String) {
  def set(changedName: String) {
    this.synchronized {
      name = changedName
    }
  }
}

//With Java 5’s change to the memory model, volatile and synchronized are basically identical except with volatile, nulls are allowed.
//
//synchronized allows for more fine-grained locking. volatile synchronizes on every access.

class Brand(@volatile var name: String) {
  def set(changedName: String) {
    name = changedName
  }
}


object SynchronizationExample extends  App {

}
