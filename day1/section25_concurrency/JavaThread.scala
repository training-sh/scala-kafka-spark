package workshop.section25_concurrency

import scala.util.control.Breaks._

class OrderProcessor extends Thread {
  override def run() {
    // runs forever
    breakable {
      while (true) {
        println("Long running queue");
        break;
      }
    }

    println("Done with thread");

  }
}

object JavaThreadExample extends  App {

  val thread = new OrderProcessor()
  thread.start()
  // waits for the thread to finish
  thread.join()
}
