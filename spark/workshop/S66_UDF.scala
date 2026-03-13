package spark.workshop

package spark.workshop

import org.apache.spark.sql.{SparkSession, functions => F}
import org.apache.spark.sql.types._

/*

/opt/spark/bin/spark-submit \
  --class spark.workshop.S85_UDF \
  --master spark://spark-master:7077 \
  /jars/scala-kafka-spark-workshop_2.12-0.1.0-SNAPSHOT.jar

*/

object S85_UDF {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("S85_UDF")
      .master("local")
      .getOrCreate()

    import spark.implicits._

    println("=== UDF power ===")

    // UDF function
    val power = (n: Long) => n * n

    val powerUdf = F.udf(power)

    // register UDF for SQL
    spark.udf.register("power", power)

    spark.sql("SELECT power(5)").show()


    println("=== Create DataFrame ===")

    val orders = Seq(
      (1, "iPhone", 100, 1000.0, 2, 5.0, 18.0),
      (2, "Galaxy", 200, 800.0, 1, 8.0, 22.0)
    )

    val orderDf = orders.toDF(
      "product_id",
      "product_name",
      "brand_id",
      "price",
      "qty",
      "discount",
      "taxp"
    )

    orderDf.show()


    println("=== calculateAmount UDF ===")

    def calculateAmount(
                         price: Double,
                         qty: Int,
                         discount: Double,
                         taxp: Double
                       ): Double = {

      var a = price * qty
      a = a - (a * discount / 100)
      val amount = a + a * taxp / 100

      println("amount is " + amount)

      amount
    }

    val calculateUdf = F.udf(calculateAmount _)

    spark.udf.register("calculate", calculateAmount _)


    println("=== UDF in DataFrame ===")

    val df1 = orderDf.withColumn(
      "amount",
      calculateUdf(
        F.col("price"),
        F.col("qty"),
        F.col("discount"),
        F.col("taxp")
      )
    )

    df1.printSchema()
    df1.show()


    println("=== Temp View ===")

    orderDf.createOrReplaceTempView("orders")


    println("=== UDF in SQL ===")

    val df2 = spark.sql(
      """
        SELECT *,
               calculate(price, qty, discount, taxp) AS amount
        FROM orders
      """
    )

    df2.printSchema()
    df2.show()


    spark.stop()

  }

}