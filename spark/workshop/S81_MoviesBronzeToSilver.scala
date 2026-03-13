package spark.workshop

import org.apache.spark.sql.types._
import org.apache.spark.sql.{functions => F}

//https://grouplens.org/datasets/movielens/
// https://files.grouplens.org/datasets/movielens/ml-latest-small.zip
//  this  has movies.csv, ratings.csv, tags.csv, ??imdb.csv

/*

/opt/spark/bin/spark-submit \
  --class spark.workshop.MoviesBronzeToSilver \
  --master spark://spark-master:7077 \
  /jars/scala-kafka-spark-workshop_2.12-0.1.0-SNAPSHOT.jar

 */


// ETL - Extract the csv data/raw data,  Transform (add ingestion time)/convert format to parquet, Load parquet data into data lake

object S81_MoviesBronzeToSilver {

  def main(args: Array[String]): Unit = {
   // .config("spark.local.dir", "C:/spark-temp")

    val spark = SparkBuilder.build("MoviesBronzeToSilver")

    // Schema definition
    val schema = StructType(Seq(
      StructField("movieId", IntegerType, nullable = false),
      StructField("title", StringType, nullable = true),
      StructField("genres", StringType, nullable = true)
    ))

    val bronzePath = "s3a://datalake/bronze/movies/movies.csv"
    val silverPath = "s3a://datalake/silver/movies/"

    // Read CSV from bronze
    // Batch - load the data once, process teh data once, output the data once
    val moviesDf = spark.read // spark.read - batch processing
      .option("header", "true") // skip the header in the csv
    //  .option("inferSchema", "true")
      .schema(schema)
      .csv(bronzePath)

    println("Bronze Data")
    moviesDf.show(10, false)

    // Example small transformation (optional)
    val silverDf = moviesDf
      .withColumn("ingest_time", F.current_timestamp())

    // Write to silver as parquet
    // write. write result into target (s3/datalake, jdbc, kafka,....)
    // Without iceberg/delta table, we cannot update/delete the data in teh datalake using spark
    // with iceberg/delta, insert,update,delete, merge

    silverDf.write
      .mode("overwrite") // if any existing content, remove that content and replace
      .parquet(silverPath) // columnar data format

    println("Silver parquet written to: " + silverPath)


    spark.stop()
  }
}