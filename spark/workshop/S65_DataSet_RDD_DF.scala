package spark.workshop


import org.apache.spark.SparkConf
import org.apache.spark.sql.{SparkSession}
import org.apache.spark.sql.functions._ // import col, desc, asc, agg, count, sum .... 250 plus functions there

object S65_DataSet_RDD_DF {

  case class Product(
                      product_id: Int,
                      product_name: String,
                      price: Double,
                      brand_id: Int,
                      offer: Option[Int]
                    )
  def main(args: Array[String]): Unit = {

    val config = new SparkConf()
    // config.set("property", "value")
    config.setMaster("local").setAppName("DataSet_RDD_DF")

    // SparkSession: entry point for Spark SQL and DataFrame APIs
    val spark = SparkSession.builder()
      .config(config)
      .getOrCreate()

    val sc = spark.sparkContext
    sc.setLogLevel("WARN")

    // internally it is useful for injecting required parameters/objects, useful data converters
    import spark.implicits._

    val products = Seq(
      (1, "iPhone", 1000.0, 100, Some(0)),
      (2, "Galaxy", 545.50, 101, None),
      (3, "Pixel", 645.99, 101, None)
    )

    // No data type mentioned. Spark will infer schema from data
    val schema = Seq("product_id", "product_name", "price", "brand_id", "offer")

    val productDf = spark.createDataFrame(products).toDF(schema: _*)

    // every DataFrame has a schema, we can print it
    productDf.printSchema()

    // ASCII format output
    productDf.show() // default shows 20 records


    println("Data Set ")
    // Data Set
    val productDs =
      productDf.as[Product]

    productDs.show()



    // Dataset is Java/Scala code, compiled languages
    // no python,sql, R
    val expensive =
      productDs.filter(p => p.price > 600)

    expensive.show()

    val productRdd = productDs.rdd

    val ds2 = productRdd.toDS()

    spark.stop()
  }
}