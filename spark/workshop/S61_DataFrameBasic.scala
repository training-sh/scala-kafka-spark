package spark.workshop


import org.apache.spark.SparkConf
import org.apache.spark.sql.{SparkSession}
import org.apache.spark.sql.functions._

object S61_DataFrameBasic {

  def main(args: Array[String]): Unit = {

    val config = new SparkConf()
    // config.set("property", "value")
    config.setMaster("local").setAppName("DataFrameBasic")

    // SparkSession: entry point for Spark SQL and DataFrame APIs
    val spark = SparkSession.builder()
      .config(config)
      .getOrCreate()

    val sc = spark.sparkContext
    sc.setLogLevel("WARN")

    import spark.implicits._

    // Spark DataFrame
    // Structured Data
    // data + schema
    // schema contains columns and data types
    // data = rows with columns as per schema

    // DataFrame Core Engine and Spark SQL Core are the same
    // DataFrame internally has RDD -> RDD[Row]
    // DataFrame is only an API layer, actual data still exists on RDD

    // When we call DataFrame APIs, internally Spark builds
    // logical plan -> optimized plan -> physical plan

    // The physical plan generates JVM bytecode using Scala compiler

    // During execution, Spark still performs RDD transformations and actions
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

    // every DataFrame has RDD internally
    // DataFrame operations are APIs applied on RDD
    // DF is RDD[Row], each Row has column name and value
    productDf.rdd.collect()

    // DataFrame RDD partitions
    productDf.rdd.getNumPartitions

    // DataFrame has transformations and actions

    // filter = transformation, returns a new DataFrame (immutable)
    // transformations are lazy

    // DataFrame filter
    // returns a new DataFrame but does not execute yet
    // no job triggered because no action
    // filter API is equivalent to SQL WHERE

    val df1 = productDf.filter(productDf("price") <= 750)
    df1.printSchema()
    df1.show()

    // select API, projection
    // creates new schema with selected columns
    val df2 = productDf.select("product_name", "price")
    df2.printSchema()
    df2.show()

    // selectExpr allows SQL expressions like CAST or functions
    // SELECT upper(product_name), price * 0.9
    val df3 = productDf.selectExpr(
      "product_name",
      "upper(product_name)",
      "price",
      "price * .9"
    )

    df3.printSchema()
    df3.show()

    // selectExpr with alias
    val df4 = productDf.selectExpr(
      "product_name",
      "upper(product_name) as title",
      "price",
      "price * .9 as grand_total"
    )

    df4.printSchema()
    df4.show()

    // derive a new column called offer_price from existing column
    val df5 = productDf.withColumn("offer_price", productDf("price") * 0.9)
    df5.printSchema()
    df5.show()

    // rename column
    val df6 = productDf.withColumnRenamed("price", "total")
    df6.printSchema()
    df6.show()

    // drop column
    val df7 = productDf.drop("brand_id")
    df7.printSchema()
    df7.show()

    // filter with conditions
    // filter and where are same (alias)
    val df8 = productDf.filter(productDf("price") >= 500 && productDf("price") < 600)
    df8.printSchema()
    df8.show()

    // using where
    val df9 = productDf.where(productDf("price") >= 500 && productDf("price") < 600)
    df9.printSchema()
    df9.show()

    // SQL expression inside where
    val df10 = productDf.where("price >= 500 AND price < 600")
    df10.printSchema()
    df10.show()

    val df11 = productDf.drop("brand_id")

    productDf.printSchema()
    println("df schema")
    df11.printSchema()

    val df12 = df11.withColumn("price", df11("price") * 2)
    df12.show()

    df11.show()

    // how to reference columns in Spark
    // all columns are represented by class Column
    println(productDf("price"))

    // using function col() to reference column
    println(col("price"))

    // add new column with constant value
    // lit = literal constant
    val df13 = productDf
      .withColumn("qty", lit(4))
      .withColumn("amount", col("qty") * col("price"))

    df13.printSchema()
    df13.show()

    // sort ascending order
    val df14 = productDf.sort("price") // default ascending
    df14.show()

    // sort descending
    val df15 = productDf.sort(desc("price"))
    df15.show()

    // alternatively use column API
    val df16 = productDf.sort(productDf("price").asc)
    df16.show()

    val df17 = productDf.sort(productDf("price").desc)
    df17.show()

    // fill null values
    productDf.show()

    val df18 = productDf.na.fill(0)
    df18.show()

    // fill null only for specific columns
    productDf.show()

    val df19 = productDf.na.fill(0, Seq("offer"))
    df19.show()

    spark.stop()
  }
}