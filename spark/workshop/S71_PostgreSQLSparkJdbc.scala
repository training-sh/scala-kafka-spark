package spark.workshop


import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types._


/*

/opt/spark/bin/spark-submit \
  --class spark.workshop.PostgreSQLSparkJdbc \
  --master spark://spark-master:7077 \
  /jars/scala-kafka-spark-workshop_2.12-0.1.0-SNAPSHOT.jar

 */

import java.util.TimeZone

object S71_PostgreSQLSparkJdbc {

  def main(args: Array[String]): Unit = {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    val spark = SparkSession.builder()
      .appName("PostgreSQL Spark Example")
      .master("local[*]")
      .config("spark.driver.extraJavaOptions", "-Duser.timezone=UTC")
      .config("spark.executor.extraJavaOptions", "-Duser.timezone=UTC")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    import spark.implicits._

    // Sample product data
    val products = Seq(
      (1, "Laptop", 1200.0),
      (2, "Mouse", 25.5),
      (3, "Keyboard", 45.0),
      (4, "Monitor", 300.0),
      (5, "USB Cable", 10.0)
    )

    val df = products.toDF("id", "name", "price")

    //   "jdbc:postgresql://postgres:5432/postgres?options=-c%20TimeZone=UTC"
    // "jdbc:postgresql://postgres:5432/postgres"
    val jdbcUrl = "jdbc:postgresql://postgres:5432/postgres?options=-c%20timezone=Asia/Kolkata"

    val connectionProperties = new java.util.Properties()
    connectionProperties.setProperty("user", "postgres")
    connectionProperties.setProperty("password", "postgres")
    connectionProperties.setProperty("driver", "org.postgresql.Driver")

    // Write data to PostgreSQL
    df.write
      .mode("overwrite")
      .jdbc(jdbcUrl, "public.products", connectionProperties)

    println("Data written to PostgreSQL.")

    // Read data from PostgreSQL
    val readDf = spark.read
      .jdbc(jdbcUrl, "public.products", connectionProperties)

    println("Reading data from PostgreSQL:")

    readDf.show(false)

    spark.stop()
  }
}