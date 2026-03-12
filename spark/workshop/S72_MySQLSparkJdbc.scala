package spark.workshop


import org.apache.spark.sql.{SparkSession}
import java.util.TimeZone

/*

/opt/spark/bin/spark-submit \
  --class spark.workshop.MySQLSparkJdbc \
  --master spark://spark-master:7077 \
  /jars/scala-kafka-spark-workshop_2.12-0.1.0-SNAPSHOT.jar

 */


object S72_MySQLSparkJdbc {

  def main(args: Array[String]): Unit = {

    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))

    val spark = SparkSession.builder()
      .appName("MySQL Spark Example")
      .master("local[*]")
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

    // MySQL JDBC URL
    val jdbcUrl =
      "jdbc:mysql://mysql:3306/ecomm?serverTimezone=UTC"

    val connectionProperties = new java.util.Properties()
    connectionProperties.setProperty("user", "root")
    connectionProperties.setProperty("password", "root")
    connectionProperties.setProperty("driver", "com.mysql.cj.jdbc.Driver")

    // Write data to MySQL
    df.write
      .mode("overwrite")
      .jdbc(jdbcUrl, "products", connectionProperties)

    println("Data written to MySQL.")

    // Read data from MySQL
    val readDf = spark.read
      .jdbc(jdbcUrl, "products", connectionProperties)

    println("Reading data from MySQL:")

    readDf.show(false)

    spark.stop()
  }
}