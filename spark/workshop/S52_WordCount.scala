package spark.workshop

import org.apache.spark.sql.SparkSession

object S52_WordCount {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("WordCountRDD")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext

    val textRDD = sc.textFile("README.md")

    val wordCounts = textRDD
      .flatMap(line => line.split("\\s+"))
      .map(word => (word, 1))
      .reduceByKey(_ + _)

    wordCounts.collect().foreach(println)

    // Top N Words

    val topWords = wordCounts
      .sortBy(_._2, ascending = false)
      .take(10)

    println("Top N Words")
    topWords.foreach(println)


    spark.stop()
  }
}