package spark.workshop

import org.apache.spark.sql.{SparkSession, Row}
import org.apache.spark.sql.types._

object S60_SparkSession {

  def main(args: Array[String]): Unit = {



    // SparkSession: entry point for Spark SQL and DataFrame APIs.
    // In a single Spark driver / notebook / Spark application,
    // there can be many SparkSessions, but
    // there is only one SparkContext.

    // Create SparkSession
    val spark = SparkSession.builder()
      .appName("HelloSparkSession")
      .master("local[*]")
      .getOrCreate()

    // Spark core operations such as RDDs, partitions, actions, etc.
    // SparkSession uses the Catalyst engine, which internally
    // uses the SparkContext for low-level execution.

    val sc = spark.sparkContext
    sc.setLogLevel("WARN")

    import spark.implicits._

    // create dataframe using spark, 1 record, 2 columns
    val df = spark.createDataFrame(
      Seq((10, "F"))
    ).toDF("age", "gender")

    df.printSchema()
    df.show()  // print DataFrame as ASCII table

    // dataframe with explicit schema
    val schema = StructType(
      List(
        StructField("age", LongType, true),
        StructField("gender", StringType, true)
      )
    )

    val data = Seq(Row(10L, "F"))

    val df2 = spark.createDataFrame(
      spark.sparkContext.parallelize(data),
      schema
    )

    df2.printSchema()
    df2.show()

    // every DF has RDD internally
    val rddRows = df2.rdd.collect()
    println("RDD rows:")
    rddRows.foreach(println)

    // number of partitions
    println("Partitions: " + df2.rdd.getNumPartitions)

    // schema
    println("Schema:")
    println(df2.schema)

    // take
    println("DF take:")
    df2.take(1).foreach(println)

    // Every DataFrame internally has an RDD

    // rdd take
    println("RDD take:")
    df2.rdd.take(1).foreach(println)

    // filter using rdd
    val females = df2.rdd
      .filter(row => row.getAs[String]("gender") == "F")
      .collect()

    println("Filtered rows:")
    females.foreach(println)

    spark.stop()
  }
}