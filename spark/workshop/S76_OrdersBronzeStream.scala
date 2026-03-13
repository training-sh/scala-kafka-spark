package spark.workshop

import org.apache.spark.sql.{SparkSession, functions => F}
import org.apache.spark.sql.types._
import org.apache.spark.sql.streaming.Trigger

object S76_OrdersBronzeStream {

  def main(args: Array[String]): Unit = {

    val spark = SparkBuilder.build("OrdersBronzeStream")


    import spark.implicits._

    spark.sparkContext.setLogLevel("WARN")

    // ----------------------------
    // Schema for JSON orders
    // ----------------------------
    val orderSchema = new StructType()
      .add("orderId", StringType)
      .add("amount", DoubleType)
      .add("customerId", StringType)
      .add("country", StringType)
      .add("orderDate", TimestampType, true)

    // ----------------------------
    // Read from Kafka
    // ----------------------------
    val kafkaDF = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "broker:9092")
      .option("subscribe", "orders")
      .option("startingOffsets", "latest")
      .load()


    // ----------------------------
    // Convert value -> JSON -> columns
    // ----------------------------
    val ordersDF =
      kafkaDF
        .selectExpr("CAST(value AS STRING) as json")
        .select(
          F.from_json(F.col("json"), orderSchema).as("data")
        )
        .select("data.*")
        .withColumn("ingest_time", F.current_timestamp())



    // DEBUG OUTPUT
    val debugQuery =
      ordersDF.writeStream
        .format("console")
        .option("truncate", false)
        .outputMode("append")
        .start()


    // ----------------------------
    // Write to Bronze Lake
    // ----------------------------
    val query =
      ordersDF.writeStream
        .format("json")   // bronze raw format
        .option("path", "s3a://datalake/bronze/orders")
        .option("checkpointLocation", "s3a://datalake/checkpoints/orders-bronze")
        .outputMode("append")
        .trigger(Trigger.ProcessingTime("1 minute"))
        .start()


    query.awaitTermination()

  }

}