package workshop.section25_concurrency

import FuturePollApp.future

import scala.concurrent.Promise
import scala.util.{Failure, Random, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object  PromiseExample extends  App {
  val r = new Random()
  def getEvenNumber() = {
    val promise = Promise[Int]()

    val value = r.nextInt(100);
    println("Value generated ", value);

    if (value % 2 == 0)
      promise.success(value)
    else
      promise.failure(new Exception("Bad"))

    promise.future
  }

  val future = getEvenNumber()

  future.onComplete {
    case Success(c) => println("yay!", c)
    case Failure(exception) => println("On no!", exception)
  }
}
