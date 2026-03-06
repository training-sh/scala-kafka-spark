package workshop.section08_patterns

object MatchCaseApp extends App  {

  def withYield = {
    val divisible = for(i <- (_:Range)) yield {
      i match {
        case i if(i % 15 == 0) => s"$i  % 15"
        case i if(i % 7 == 0) => s"$i  % 7"
        case _ => i
      }
    }

    divisible(1 to 100) foreach (println _ )

    divisible(101 to 130) foreach (println _ )
  }


  withYield

}
