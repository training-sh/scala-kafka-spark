package day1



object S07_Object extends  App {

  // Singleton : Only one instance of class allowed, part of design patterns
  // Language does not support natively, for ex. in Java, we make constructor private,
                                                // static functions to create object

  // In Scala: Singleton is default feature,
  // Object are singleton instance already, no class for that
  // replacement for java static feature, singleton features
  // We cannot create object/instances for Object
  object Logger {
      // object constructor, executed on first use
    // object initialization
    println("Logger constructor") // called only one per application

    var level: Int = 0

    def info(msg: String) = {
      println(s"Info $level, $msg")
    }
    def warn(msg: String) = {
      println(s"Warn $level, $msg")
    }
  }

  // outside logger
  println("Main")

  // use Logger, lazily initialized only if we use them , initialized only once
  //Logger // first use
  // Logger.level = 1 // first use

  Logger.info("Starting application")
  Logger.warn("not enough ram")
}
