package workshop.s030_std_file_io

import scala.io.Source

object ReadFile extends  App {

  val filename = "README.md"
  // getLines returns an iterator
  for (line <- Source.fromFile(filename).getLines) {
    println(line)
  }

  // to list or array
  val lines1 = Source.fromFile("README.md").getLines.toList
  val lines2 = Source.fromFile("README.md").getLines.toArray

  // read content of the files into a string
  val fileContents = Source.fromFile(filename).getLines.mkString

  println("BEGIN//")
  println(fileContents);

  println("//END")


  // Close the file properly

  val bufferedSource = Source.fromFile("README.md")
  for (line <- bufferedSource.getLines) {
    println(line.toUpperCase)
  }

  bufferedSource.close

}
