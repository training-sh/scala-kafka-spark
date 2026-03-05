package workshop.s001_main

//Go to edit configuration
// Set Program Arguments Welcome to Scala

object MainArgs {
  def main(args: Array[String]): Unit = {
    println("Command line Arguments " + args.length);
    println("Arg 0 = " +  args(0));
    println("Arg 1 = " + args(1));
    println("Arg 2 = " + args(2));
  }
}
