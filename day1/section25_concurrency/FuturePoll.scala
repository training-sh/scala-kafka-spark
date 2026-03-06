package workshop.section25_concurrency

import FutureBasicsApp.getUser

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object FuturePollApp extends  App {

  val future = Future {
     var count = 0;
    println("Processing ", count);
    Thread.sleep(1000);
    count += 1

    println("Processing ", count);
    Thread.sleep(1000);
    count += 1

    println("Processing ", count);
    Thread.sleep(1000);
    count += 1

    println("Processing ", count);
    Thread.sleep(1000);
    count += 1

    println("Processing ", count);
    Thread.sleep(1000);
    count += 1

    println("Processing ", count);
    Thread.sleep(1000);
    count

  }

  future.onComplete {
    case Success(c) => println("yay!", c)
    case Failure(exception) => println("On no!", exception)
  }

  while(!future.isCompleted) {
    println("IsFuture Completed ", future.isCompleted)
    Thread.sleep(1000)
  }


  println("Future done");
  future.map ( result => println("Result is ", result));

}
