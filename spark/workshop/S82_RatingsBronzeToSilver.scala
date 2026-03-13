package spark.workshop


package spark.workshop

import org.apache.spark.sql.types._
import org.apache.spark.sql.{functions => F}

/*

/opt/spark/bin/spark-submit \
  --class spark.workshop.S82_RatingsBronzeToSilver \
  --master spark://spark-master:7077 \
  /jars/scala-kafka-spark-workshop_2.12-0.1.0-SNAPSHOT.jar

*/

object S82_RatingsBronzeToSilver {

  def main(args: Array[String]): Unit = {

    val spark = SparkBuilder.build("RatingsBronzeToSilver")

    // Schema definition
    val schema = StructType(Seq(
      StructField("userId", IntegerType, nullable = false),
      StructField("movieId", IntegerType, nullable = false),
      StructField("rating", DoubleType, nullable = true),
      StructField("timestamp", LongType, nullable = true) // epoch seconds
    ))

    val bronzePath = "s3a://datalake/bronze/ratings/ratings.csv"
    val silverPath = "s3a://datalake/silver/ratings/"

    // Read CSV from bronze
    val ratingsDf = spark.read
      .option("header", "true")
      .schema(schema)
      .csv(bronzePath)

    println("Bronze Ratings")
    ratingsDf.show(10, false)

    //timestamp is in seconds since 1970, not in milli seconds

    // Convert timestamp → proper datetime
    val silverDf = ratingsDf
      .withColumn(
        "rating_time",
        F.to_timestamp(F.from_unixtime(F.col("timestamp")))
      )
      .withColumn(
        "ingest_time",
        F.current_timestamp()
      )
      .drop("timestamp") // optional, remove raw timestamp


    println("Silver Ratings")
    silverDf.show(10, false)


    // Write parquet to silver
    silverDf.write
      .mode("overwrite")
      .parquet(silverPath)

    println("Silver parquet written to: " + silverPath)

    spark.stop()
  }
}