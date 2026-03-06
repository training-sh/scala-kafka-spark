package workshop.section25_concurrency

import scala.concurrent.{Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scala.util.Random

object NonBlockingFuture extends App {
  println("starting calculation ...")
  val f = Future {
    Thread.sleep(Random.nextInt(500))
    42
    //throw new Exception("Unknwon Error ")
  }
  println("before onComplete")

  // or f onComplete works too

  f.onComplete {
    case Success(value) => println(s"Got the callback, meaning = $value")
    case Failure(e) => println("Error "); e.printStackTrace
  }
  // do the rest of your work
  println("A ..."); Thread.sleep(100)
  println("B ..."); Thread.sleep(100)
  println("C ..."); Thread.sleep(100)
  println("D ..."); Thread.sleep(100)
  println("E ..."); Thread.sleep(100)
  println("F ..."); Thread.sleep(100)
  Thread.sleep(2000)
}