package spark.workshop


import org.apache.spark.sql.SparkSession

/*

/opt/spark/bin/spark-submit \
  --class spark.workshop.S67_GlobalTempView \
  --master spark://spark-master:7077 \
  /jars/scala-kafka-spark-workshop_2.12-0.1.0-SNAPSHOT.jar

*/

object S67_GlobalTempView {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("S86_GlobalTempView")
      .master("local")
      .getOrCreate()

    import spark.implicits._

    println("=== Create DataFrame ===")

    val data = Seq(
      (1, "Apple"),
      (2, "Samsung"),
      (3, "Nokia")
    )

    val df = data.toDF("id", "brand")

    df.show()


    // -----------------------------
    // temp view
    // -----------------------------

    println("=== TEMP VIEW ===")

    df.createOrReplaceTempView("brands")

    spark.sql("SELECT * FROM brands").show()


    // -----------------------------
    // global temp view
    // -----------------------------

    println("=== GLOBAL TEMP VIEW ===")

    df.createOrReplaceGlobalTempView("brands_global")

    spark.sql(
      """
        SELECT *
        FROM global_temp.brands_global
      """
    ).show()


    // -----------------------------
    // new SparkSession
    // -----------------------------

    println("=== NEW SESSION ===")

    val spark2 = spark.newSession()

    println("Temp view in new session (should fail)")

    try {
      spark2.sql("SELECT * FROM brands").show()
    } catch {
      case e: Exception =>
        println("Temp view not available in new session")
    }


    println("Global temp view in new session")

    spark2.sql(
      """
        SELECT *
        FROM global_temp.brands_global
      """
    ).show()


    spark.stop()

  }

}