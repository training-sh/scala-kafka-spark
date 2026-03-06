package workshop.section25_concurrency

import scala.concurrent.Future
import scala.util.{Failure, Success, Random}
import scala.concurrent.ExecutionContext.Implicits.global

object FutureBasicsApp extends  App {

  val r = new Random()

  case class User(name: String)
  def getUser() = {
      if (r.nextInt(1000) % 2 == 0)
        User("Krish")
      else throw new Exception("Ooh odd number, no user now")
  }

  val future = Future {
    getUser()
  }

  future.onComplete {
    case Success(user) => println("yay!", user)
    case Failure(exception) => println("On no!", exception)
  }


  Thread.sleep(10000)

}
