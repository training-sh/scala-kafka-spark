package spark.workshop

import org.apache.spark.sql.SparkSession

object S51_RddBasic {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("RDD Concepts Demo")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext

    // reduce Spark logs
    sc.setLogLevel("WARN")

    println("========== parallelize ==========")

    // Create RDD from numbers 0 to 10
    val numbersRDD = sc.parallelize(0 to 10)

    println("Partitions : " + numbersRDD.getNumPartitions)

    println("\n========== collect ==========")

    // collect returns all elements to driver
    numbersRDD.collect().foreach(println)

    println("\n========== take ==========")

    // take first N elements
    numbersRDD.take(5).foreach(println)

    println("\n========== sum ==========")

    val sumValue = numbersRDD.sum()
    println("Sum = " + sumValue)

    println("\n========== min / max ==========")

    println("Min = " + numbersRDD.min())
    println("Max = " + numbersRDD.max())

    println("\n========== reduce (sum) ==========")

    val sumUsingReduce = numbersRDD.reduce((a, b) => a + b)
    println("Sum using reduce = " + sumUsingReduce)

    println("\n========== reduce (average) ==========")

    val avg = numbersRDD.reduce(_ + _).toDouble / numbersRDD.count()
    println("Average = " + avg)

    println("\n========== filter (odd numbers) ==========")

    val oddRDD = numbersRDD.filter(x => x % 2 != 0)

    oddRDD.collect().foreach(println)

    println("\n========== map (Celsius → Fahrenheit) ==========")

    val fahrenheitRDD = numbersRDD.map(c => (c * 9.0 / 5) + 32)

    fahrenheitRDD.collect().foreach(println)

    println("\n========== repartition ==========")

    val repartRDD = numbersRDD.repartition(4)

    println("Partitions after repartition = " + repartRDD.getNumPartitions)

    val glomResult = repartRDD
      .glom()
      .collect()

    for (partition <- glomResult) {
      println("glomResult =", partition.mkString(", "))
    }


    //println("glomResult = " + glomResult)

    // Or ZipWithIndex, make a tuple with index and list element
    // runs inside driver, not on executor, as glom().collect() itself action
    // This is simple list manipulation, not spark task
    glomResult.zipWithIndex.foreach { case (partitionData, index) =>
      println(s"Partition $index -> ${partitionData.mkString(", ")}")
    }


    println("\n========== coalesce ==========")

    val coalesceRDD = repartRDD.coalesce(2)

    println("Partitions after coalesce = " + coalesceRDD.getNumPartitions)


    println("Total Partitions = " + numbersRDD.getNumPartitions)

    println("\n===== FOREACH =====")

    numbersRDD.foreach { n =>

      val pid = ProcessHandle.current().pid()
      val thread = Thread.currentThread().getName

      println(s"Value=$n  PID=$pid  Thread=$thread")

      Thread.sleep(500)
    }

    println("\n===== FOREACH PARTITION =====")

    // partition is iterators, refers to all elements in each partitions
    numbersRDD.foreachPartition { partition =>

      val pid = ProcessHandle.current().pid()
      val thread = Thread.currentThread().getName

      println(s"\nPartition started -> PID=$pid Thread=$thread")

      partition.foreach { n =>

        val threadInside = Thread.currentThread().getName

        println(s"Value=$n PID=$pid Thread=$threadInside")

        Thread.sleep(500)
      }
    }

   // spark.stop()


    println("\nSpark UI available at http://localhost:4040")
    println("Press Ctrl+C to exit...")

    // Shutdown hook
    sys.addShutdownHook {
      println("Shutdown hook triggered. Stopping Spark...")
      spark.stop()
    }

    // keep program alive
    while (true) {
      Thread.sleep(10000)
    }

  }
}