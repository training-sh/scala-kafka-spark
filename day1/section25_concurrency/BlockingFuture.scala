package workshop.section25_concurrency

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


object BlockingFuture  extends App{

  // used by 'time' method
  implicit val baseTime = System.currentTimeMillis

  // 2 - create a Future / Task
  val f = Future {
    println("Future running")
    Thread.sleep(5000)
    println("future done")
    1 + 1
  }

  println("Main thread");
  // 3 - this is blocking (blocking is bad)
  val result = Await.result(f, 10 second)
  println(result)
  Thread.sleep(1000)

}
