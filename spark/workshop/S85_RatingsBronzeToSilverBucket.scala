package spark.workshop
  

import org.apache.spark.sql.types._
import org.apache.spark.sql.{functions => F}

/*

/opt/spark/bin/spark-submit \
  --class spark.workshop.S85_RatingsBronzeToSilverBucket \
  --master spark://spark-master:7077 \
  /jars/scala-kafka-spark-workshop_2.12-0.1.0-SNAPSHOT.jar

*/

object S85_RatingsBronzeToSilverBucket {

  def main(args: Array[String]): Unit = {

    val spark =
      SparkBuilder.build("RatingsBronzeToSilverBucket")

    import spark.implicits._

    // schema
    
    val schema = StructType(Seq(
      StructField("userId", IntegerType, false),
      StructField("movieId", IntegerType, false),
      StructField("rating", DoubleType, true),
      StructField("timestamp", LongType, true)
    ))

    val bronzePath =
      "s3a://datalake/bronze/ratings/ratings.csv"

    val silverPath =
      "s3a://datalake/silver/ratings_bucket/"

    
    // namespace
    

    spark.sql(
      """
      CREATE DATABASE IF NOT EXISTS movielens
      """
    )

    spark.sql("USE movielens")

    
    // read bronze
    

    val bronzeDf = spark.read
      .option("header", "true")
      .schema(schema)
      .csv(bronzePath)

    println("Bronze")
    bronzeDf.show(5, false)

    
    // transform
    

    val silverDf =
      bronzeDf
        .withColumn(
          "rating_time",
          F.to_timestamp(
            F.from_unixtime(F.col("timestamp"))
          )
        )
        .withColumn(
          "ingest_time",
          F.current_timestamp()
        )
        .drop("timestamp")

    println("Silver")
    silverDf.show(5, false)


    
    // drop table if exists
    

    spark.sql(
      """
      DROP TABLE IF EXISTS movielens.silver_ratings_bucket
      """
    )


    
    // write bucket table
    // ORC + Hive can give good performance as it has bucket, sort and partition
    // since it sorts, it has internal stripe index, row group index, min/max, bloom filters
    // so hive avoid unnessary shuffles

    // ORC generally very good for bucket, as it has features for index..

    silverDf.write
      .format("orc")   // orc   or parquet
      .mode("overwrite")
      .option("path", silverPath)
      .bucketBy(32, "movieId")
      .sortBy("movieId")
      .saveAsTable(
        "movielens.silver_ratings_bucket"
      )


    println("Bucket table written")

    // using buckets in spark, you need this for your config, else spark will not consider bucket

//    spark.conf.set("spark.sql.autoBroadcastJoinThreshold", -1)
//    spark.conf.set("spark.sql.join.preferSortMergeJoin", "true")
//    spark.conf.set("spark.sql.sources.bucketing.enabled", "true")
//    spark.conf.set("spark.sql.optimizer.bucketedScanEnabled", "true")


    
    // verify
    

    spark.sql(
      """
      SELECT *
      FROM movielens.silver_ratings_bucket
      LIMIT 5
      """
    ).show(false)


    
    // show tables
    

    spark.sql("SHOW TABLES").show(false)


    spark.sql(
      """
        DESCRIBE EXTENDED movielens.silver_ratings_bucket
      """
    ).show(false)

    spark.sql(
      """
  DESCRIBE FORMATTED movielens.silver_ratings_bucket
  """
    ).show(200, false)

    // go to the location in the data lake, look for _00000, _00002, _00003 etc
    // part-00000-xxxxxxxx-d6b5-430a-9afeyyyyyyy_00000.c000.snappy.parquet
    // part-00000-xxxxxxxx-d6b5-430a-9afeyyyyyyy_00001.c000.snappy.parquet

    spark.stop()
  }

}