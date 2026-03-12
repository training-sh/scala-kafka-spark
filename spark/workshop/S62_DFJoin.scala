package spark.workshop

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object S053_DFJoin {

  def main(args: Array[String]): Unit = {

    val config = new SparkConf()
    config.setMaster("local")
    config.setAppName("DataFrameJoin")

    // SparkSession: entry point for Spark SQL and DataFrame APIs
    val spark = SparkSession.builder()
      .config(config)
      .getOrCreate()

    val sc = spark.sparkContext
    sc.setLogLevel("WARN")

    import spark.implicits._

    // -----------------------------------------
    // DataFrames
    // -----------------------------------------
    // DataFrame = structured data
    // data + schema
    // schema contains columns and data types

    // Create products DataFrame
    val products = Seq(
      // (product_id, product_name, price, brand_id)
      (1, "iPhone", 1000.0, 100),
      (2, "Galaxy", 545.50, 101),
      (3, "Pixel", 645.99, 101)
    ).toDF("product_id", "product_name", "price", "brand_id")

    products.printSchema()
    products.show()

    // Create brands DataFrame
    val brands = Seq(
      // (brand_id, brand_name)
      (100, "Apple"),
      (101, "Samsung")
    ).toDF("brand_id", "brand_name")

    brands.printSchema()
    brands.show()

    // -----------------------------------------
    // Join
    // -----------------------------------------
    // Join combines rows from two DataFrames
    // based on a join condition

    // Inner Join (default join)
    // Only matching rows are returned
    val df1 = products.join(brands, "brand_id")

    df1.printSchema()
    df1.show()

    // Explicit inner join
    val df2 = products.join(brands, Seq("brand_id"), "inner")

    df2.printSchema()
    df2.show()

    // -----------------------------------------
    // Left Join
    // -----------------------------------------
    // Returns all rows from left DataFrame
    // matching rows from right DataFrame
    // non-matching rows will contain NULL

    val df3 = products.join(brands, Seq("brand_id"), "left")

    df3.printSchema()
    df3.show()

    // -----------------------------------------
    // Right Join
    // -----------------------------------------
    // Returns all rows from right DataFrame

    val df4 = products.join(brands, Seq("brand_id"), "right")

    df4.printSchema()
    df4.show()

    // -----------------------------------------
    // Full Join
    // -----------------------------------------
    // Returns rows from both sides
    // unmatched rows will contain NULL

    val df5 = products.join(brands, Seq("brand_id"), "outer")

    df5.printSchema()
    df5.show()

    // -----------------------------------------
    // Join using expression
    // -----------------------------------------
    // Sometimes column names are different,
    // so we write join condition explicitly

    val df6 = products.join(
      brands,
      products("brand_id") === brands("brand_id"),
      "inner"
    )

    df6.printSchema()
    df6.show()

    // -----------------------------------------
    // Select columns after join
    // -----------------------------------------
    // Join may produce duplicate columns.
    // We can select only required columns.

    val df7 = products
      .join(brands, products("brand_id") === brands("brand_id"))
      .select(
        products("product_name"),
        products("price"),
        brands("brand_name")
      )

    df7.printSchema()
    df7.show()

    // -----------------------------------------
    // Alias example
    // -----------------------------------------
    // Aliasing helps when both tables have same column names

    val p = products.alias("p")
    val b = brands.alias("b")

    val df8 = p.join(
        b,
        col("p.brand_id") === col("b.brand_id"),
        "inner"
      )
      .select(
        col("p.product_name"),
        col("p.price"),
        col("b.brand_name")
      )

    df8.printSchema()
    df8.show()

    spark.stop()
  }
}