package workshop.section25_concurrency

import java.util.concurrent.{Executors, ExecutorService}

case class Order();

class OrderProcessorRunnable(order: Order) extends  Runnable {
  override def run() {
    println("Running run method");
  }
}

object RunnableExample extends  App {
  val pool = Executors.newFixedThreadPool(2) // 2 threads
  pool.submit(new OrderProcessorRunnable(new Order()))
}