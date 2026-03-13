package spark.workshop


package spark.workshop

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

/*

/opt/spark/bin/spark-submit \
  --class spark.workshop.S68_BatchOutputModes \
  --master spark://spark-master:7077 \
  /jars/scala-kafka-spark-workshop_2.12-0.1.0-SNAPSHOT.jar

*/

object S68_BatchOutputModes {

  def main(args: Array[String]): Unit = {

    val spark = SparkBuilder.build("BatchOutputModes")

    import spark.implicits._

    spark.sparkContext.setLogLevel("WARN")

    
    // Hardcoded customers data
    

    val customers = Seq(
      (1, "Dravid", "Bangalore"),
      (2, "Ravi", "Chennai"),
      (3, "Rohit", "Delhi")
    ).toDF("id", "name", "city")

    customers.show(false)
    
    // Paths
    
    val appendPath =
      "s3a://datalake/bronze/customers-append"

    val overwritePath =
      "s3a://datalake/bronze/customers-overwrite"

    val ignorePath =
      "s3a://datalake/bronze/customers-ignore"

    val errorPath =
      "s3a://datalake/bronze/customers-error"

    val updatePath =
      "s3a://datalake/bronze/customers-update"


    
    // 1. APPEND MODE, old file remains as is, new fiels added

    println("=== APPEND MODE ===")

    customers.write
      .format("json")
      .mode("append")
      .save(appendPath)


    
    // 2. OVERWRITE MODE, old files removed
    

    println("=== OVERWRITE MODE ===")

    customers.write
      .format("json")
      .mode("overwrite")
      .save(overwritePath)



    // 3. IGNORE MODE, if fiels exists will not do anything, ingore, means no write
    //  if files exists will not do anything, ingore, means no write
    println("=== IGNORE MODE ===")

    customers.write
      .format("json")
      .mode("ignore")
      .save(ignorePath)


    // 4. ERROR MODE, throw error if file exists

    println("=== ERROR MODE ===")

    try {

      customers.write
        .format("json")
        .mode("error")
        .save(errorPath)

    } catch {
      case e: Exception =>
        println("ERROR mode failed as expected")
        println(e.getMessage)
    }

    // 5. SIMULATED UPDATE (BATCH)
    

    // update not supported in batch
    // so we do:
    //
    // read old
    // union new
    // dropDuplicates(id)
    // overwrite


    println("=== SIMULATED UPDATE ===")

    val newCustomers = Seq(
      (2, "Azar", "Hyderabad"),   // updated city
      (4, "Sachin", "Mumbai")     // new row
    ).toDF("id", "name", "city")


    // Typical workaround for Spark without iceberg/delta table/classical path
    // iceberg/delta table support update, delete and merge.
    // without iceberg/delta table, we cannot update or delete existing data with update/delete construct in sql / df
    val finalDF: DataFrame =

      try {

        val oldDF = spark.read
          .format("json")
          .load(updatePath)

        oldDF
          .union(newCustomers)
          .dropDuplicates("id")

      } catch {

        case _: Exception =>
          newCustomers
      }


    finalDF.show(false)


    finalDF.write
      .format("json")
      .mode("overwrite")
      .save(updatePath)



    println("=== DONE ===")

    spark.stop()

  }

}
