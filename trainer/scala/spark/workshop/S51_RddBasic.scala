package spark.workshop

import org.apache.spark.sql.SparkSession

object S51_RddBasic {
  def main(args: Array[String]): Unit = {

    // Driver - Brain, Orchestrate entire execution
    // Executor - perform the task, join, sort, filter, transformation, read file, write to file

    //  This is Drive part of the code
    // Local, in single JVM Process , Driver, Executor shall run, not scalable, good debugging, development
    // Local[*] - * about number of parallelism, how many parallel jobs can be run
    // local[1] - 1 task at a time
    // local[4] - 4 tasks at a time
    // Local[*] - here * means number of cpu cores, 6 physical core, 12 logical core

    // spark - spark session, entry point for Spark SQL, structured data processing, data frame, etc
    // one or  more spark session per spark driver
    val spark = SparkSession.builder()
      .appName("RDD Concepts Demo") // useful in logging, spark ui for metrics, performance analysise, spark history server
      .master("local[1]") // Local means dev env,
      .getOrCreate()

    // sc here spark context, is an entry for spark core
    // RDD, DAG, Stage, shuffling, task, task queue, stage queue,
    // THERE WILL BE ONLY ONE SPARK CONTEXT per Driver
    val sc = spark.sparkContext

    // reduce Spark logs
    // generate lots of logs, we want only WARN or error, no info
    sc.setLogLevel("WARN") // initial bootstrap INFO, then only WARN will be shown


    println("========== parallelize ==========")
    // Create RDD from numbers 0 to 10

    // Data Source input, hardcode, 0 to 10 inclusive
    // parallelize - lazy evaluation
    // RDD/DF ARE IMMUTABLE, PARTITION ARE IMMUTABLE
    // WE CANNOT MODIFY DATA IN-PLACE IN PARTITION
    // Every RDD/DF transformation will produce new RDD/DF, new partitions
    val numbersRDD = sc.parallelize(0 to 10)

    //  local[*]  - I have 12 logical cores,  12 partition, each partition shall have 1 task
    // 1 partition = 1 task
    // 4 partitions = 4 tasks
    // 1000 partitions = 1000 tasks
    // FOR EACH PARTITION, it will create 1 TASK, TASK shall be put into queue, scheduled , executed inside executor

    println("Partitions : " + numbersRDD.getNumPartitions)

    // SOURCE: parallelize, spark.read...
    // TRANSFORMATION, map, filter, flatMap,..join,.. LAZY eval
    // Action - Result oriented, it runs the job, produce result
    // Action, will actually perform data processing [collect, take, foreach, reduce, foreachPartition, write...)



    println("\n========== collect ==========")

    // collect returns all elements to driver
    // collect is an Action function, it perform action, that execute data processing logics
    // collect make use of executors
    // here now, the data 0 to 10, shall be loaded into exeuctors memory , known as partition
    // now driver, shall collect those data from partition to driver, no transformation here, load data into partition, read data
    val result = numbersRDD.collect() // Job 1

    // collect , or any action function
    // Every action, create A JOB
    // JOB converts RDD Tree into DAG Directed Acyclic Graph
    // DAG Scheduler, will exeucute DAG Graph
    // Job create stages
    // each stage shall one or more DAG
    // each stage will task
    // Task shall be scheduled by task manager
    // Task manager send task to executor

    // executor execute the task
    // executor return result to driver/write to db/write to s3

    result.foreach(println)


    // Write a transformation
    // Filter odd number
    def odd (n: Int): Boolean = {
      val threadInside = Thread.currentThread().getName
      val tid = Thread.currentThread().getId
      val pid = ProcessHandle.current().pid()

      println(s"Value=$n PID=$pid  ThreadId=$tid Thread=$threadInside")

      n % 2 == 1 // return value
    }

    // RDD Lineage
    // numbersRDD is parent rdd
    // oddRdd is child or drived rdd
    // It is tree model
    // LAzy val, transfomration, no action performed
    val oddRdd = numbersRDD.filter(odd)

    // the numbers 0 to 10 laoded into executor
    // and 12 tasks created, each shall be executed inside executor
    // we will get only odd numbers
    val r2 = oddRdd.collect ()  // JOB 2

    r2.foreach(println)

    // problem with collect action
    // bring all the results in teh partitions in the executor to driver program
    // Executors are scalable, you run inside cluster, you may have 1 or 10 or more executors
    // can haves lot of ram, cpu , because they run in cluster
    // Executor has fault tolerance,
    // Resilient: if one executor fails, one task fail, the task shall rerun on another executor



    // Driver shall be limited machine, like developer laptop/scheduler application
    // Driver is not scalable, does not run in cluster
    // Driver does not have fault tolerance , if it crash, the entire program terminate


    println("\n========== take ==========")

    // take first N elements
    numbersRDD.take(5).foreach(println)


    println("\n========== sum ==========")

    val sumValue = numbersRDD.sum() // Action
    println("Sum = " + sumValue)

    println("\n========== min / max ==========")

    println("Min = " + numbersRDD.min()) // Action
    println("Max = " + numbersRDD.max())  // Action


    println("\n========== reduce (sum) ==========")

    // reduce (acc , b) ==> acc + b
    // accumulator
    // b is actual number from partition 0, 1,2,3,4,5,.d..10
    // (acc = 0, b = 0 ) => 0 + 0 ==> result is 0, now this result shall be used as input for next call
    // (acc = 0, b = 1) => 0 + 1 => result (1), now 1 shall be acc
    // (acc = 1, b= 2) => 1 + 2 => result (3), now 3 will be acc
    // (acc= 3, b=3) => 3 + 3 => result (6), now 6 will be acc
    // 6, 4
    val sumUsingReduce = numbersRDD.reduce((acc, b) => acc + b) // action
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

    // in general used to increase teh partition, but you can also use to reduce partition
    // does random shuffle input partition, move data into new partitions
    // best pracitce, used for increase partition
    // local[1], numbersRDD is one partition, it has 11 numbers 0...to..10
    // 1 partition = 1 task, bad for parallelism
    // reuse repartition, to distribute data from 1 partition to 4 partitions
    // still numbersRDD 1 partition, because it is immutable
    // repartRDD shall be having 4 partitions, the distributed amoung them
    // 4 partitions = 4 parallel tasks
    val repartRDD = numbersRDD.repartition(4) // LAZY EVAL, not ACTION
    println("Partitions after repartition = " + repartRDD.getNumPartitions)

    //repartRDD.sum() create ONE JOB, it has two stages
    // Stage 0 - Load the data [0 to 10] into 1 partitions , parallelize (0,..10)   - 1 PART - 1 TASK
    // Stage 1 - repartition, move data into 4 differnet partitions, SHUFFLING , perform sum over partitions , 4 PARTs = 4 TASKS
    // Total 2 stages , total task 1 + 4 = 5 tasks

    println ("sum over repartRdd ", repartRDD.sum()) // Spark UI, check number of tasks 4


    // rdd.collect // collect data from all partition, merge them into one list
    // rdd.glom() // collect data from all partitions, return as list of list


    val glomResult = repartRDD
      .glom()
      .collect() // collect data from each partitions

    for (partition <- glomResult) {
      println("repartRDD glomResult =", partition.mkString(", "))
    }

    println("=====numRdd glom")

    for (partition <- numbersRDD.glom().collect()) {
      println("numbersRDD glomResult =", partition.mkString(", "))
    }




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
