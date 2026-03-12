package spark.workshop

import org.apache.spark.sql.SparkSession

object SparkBuilder {


  val base: Map[String,String] = Map(
    "spark.sql.warehouse.dir" -> "s3a://warehouse/"
  )

  val minio: Map[String,String] = Map(
    "spark.hadoop.fs.s3a.endpoint" -> "http://minio:9000",
    "spark.hadoop.fs.s3a.endpoint.region" -> "us-east-1",
    "spark.hadoop.fs.s3a.access.key" -> "minio",
    "spark.hadoop.fs.s3a.secret.key" -> "minio12345",
    "spark.hadoop.fs.s3a.path.style.access" -> "true",
    "spark.hadoop.fs.s3a.impl" -> "org.apache.hadoop.fs.s3a.S3AFileSystem",
    "spark.hadoop.fs.s3a.connection.ssl.enabled" -> "false",
    "spark.hadoop.fs.s3a.aws.credentials.provider" ->
      "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider"
  )

  val iceberg: Map[String,String] = Map(
    "spark.sql.extensions" ->
      "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions",

    "spark.sql.catalog.demo" ->
      "org.apache.iceberg.spark.SparkCatalog",

    "spark.sql.catalog.demo.type" ->
      "hadoop",

    "spark.sql.catalog.demo.warehouse" ->
      "s3a://warehouse/iceberg"
  )


  def build(app: String, master: String = "local[*]"): SparkSession = {
    val finalConfig =
       base ++
         minio ++
         iceberg

    val builder = SparkSession
      .builder()
      .appName(app)
      .master(master)

    finalConfig.foreach { case (k,v) =>
      builder.config(k,v)
    }

    val spark = builder.getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    spark
  }


  def build2(app: String, master: String = "local[*]"): SparkSession = {
    val spark = SparkSession
      .builder()
      .appName(app)
      .master(master)
      .config("spark.sql.warehouse.dir", "s3a://warehouse/")
      .config("spark.hadoop.fs.s3a.endpoint.region", "us-east-1")
      .config("spark.hadoop.fs.s3a.endpoint", "http://minio:9000")
      .config("spark.hadoop.fs.s3a.access.key", "minio")
      .config("spark.hadoop.fs.s3a.secret.key", "minio12345")
      .config("spark.hadoop.fs.s3a.path.style.access", "true")
      .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
      .config("spark.hadoop.fs.s3a.connection.ssl.enabled", "false")
      .config(
        "spark.hadoop.fs.s3a.aws.credentials.provider",
        "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider"
      )
      .getOrCreate()
    /*
    Level	Meaning
      ALL	everything
      DEBUG	debugging
      INFO	default (too noisy)
    WARN	warnings
      ERROR	errors only
    */
    spark.sparkContext.setLogLevel("WARN")

    spark
  }
}
