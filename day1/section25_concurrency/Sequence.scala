package workshop.section25_concurrency

import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext.Implicits.global


object SequenceExample extends  App {



    val task1 = Future {
      Thread.sleep(2000)
      println("task1 is completed")
      "Task 1 result"
    }


  val task2 = Future {
    Thread.sleep(3000)
    println("task2 is completed")
    "Task 2 result"
  }


  val task3WithError = Future {
      Thread.sleep(4000)
      println("Task3 is completed")
      throw new Exception("Exception with task3")
    }

    val task4 = Future {
      Thread.sleep(10000)
      println("task2 is completed")
      "Task 4 Result"
    }

   // val allTasks = Future.sequence(List(task1, task2, task3WithError, task4))
   val allTasks = Future.sequence(List(task1, task2, task4))

    allTasks.onComplete {
      case Success(res) => println("Success: " + res)
      case Failure(ex)  => println("Ohhh Exception: " + ex.getMessage)
    }

    Await.ready(allTasks, 20.seconds)

    println(s"All Future results are: ${allTasks.value}")
    Thread.sleep(2000)

}