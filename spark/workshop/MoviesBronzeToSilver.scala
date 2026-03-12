package spark.workshop

import org.apache.spark.sql.{SparkSession, functions => F}
import org.apache.spark.sql.types._

//https://grouplens.org/datasets/movielens/
// https://files.grouplens.org/datasets/movielens/ml-latest-small.zip
//  this  has movies.csv, ratings.csv, tags.csv, ??imdb.csv

/*

/opt/spark/bin/spark-submit \
  --class spark.workshop.MoviesBronzeToSilver \
  --master spark://spark-master:7077 \
  /jars/scala-kafka-spark-workshop_2.12-0.1.0-SNAPSHOT.jar

 */

object MoviesBronzeToSilver {

  def main(args: Array[String]): Unit = {
   // .config("spark.local.dir", "C:/spark-temp")

    val spark = SparkBuilder.build("MoviesBronzeToSilver")

    import spark.implicits._

    // Schema definition
    val schema = StructType(Seq(
      StructField("movieId", IntegerType, nullable = false),
      StructField("title", StringType, nullable = true),
      StructField("genres", StringType, nullable = true)
    ))

    val bronzePath = "s3a://datalake/bronze/movies/movies.csv"
    val silverPath = "s3a://datalake/silver/movies/"

    // Read CSV from bronze
    val moviesDf = spark.read
      .option("header", "true")
      .schema(schema)
      .csv(bronzePath)

    println("Bronze Data")
    moviesDf.show(10, false)

    // Example small transformation (optional)
    val silverDf = moviesDf
      .withColumn("ingest_time", F.current_timestamp())

    // Write to silver as parquet
    silverDf.write
      .mode("overwrite")
      .parquet(silverPath)

    println("Silver parquet written to: " + silverPath)

    spark.stop()
  }
}