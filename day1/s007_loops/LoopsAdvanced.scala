package workshop.s007_loops

object LoopsAdvanced {

  def forYieldExpr() : Unit = {
    println("For forYieldExpr")
    val list = for( i <- 1 to 5) yield i * 10;

    println("Vector ", list)
  }


  def forYieldLazyExec() : Unit = {
    println("For forYieldLazyExec")

    def multi(n : Int) : Int = {
      println ("multi called ", n)
      n * n
    };

    val list = for( i <- 1 to 5) yield multi(i);

    println("Vector ", list)
  }

  def forNested(): Unit = {
    println("For nested");
    val list = for (i <- 1 to 5; j <- 1 until i)  yield (i, j)
    println("Vector pair ", list);
  }

  def forCurly: Unit = {
    val nums = Seq(1,2,3)
    val letters = Seq('a', 'b', 'c')
    val res = for {
      n <- nums
      c <- letters
    } yield (n, c)

    println("Result ", res);
  }


  def forCurly2: Unit = {

    val res = for {
      n <- 1 to 3;
      c <- 1 to n
    } yield (n, c)

    println("Result ", res);
  }

  def main(args: Array[String]) : Unit = {
    forYieldExpr()
    forYieldLazyExec()
    forNested()
    forCurly
    forCurly2
  }
}
