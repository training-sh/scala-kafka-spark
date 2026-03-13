package spark.workshop

package spark.workshop

import org.apache.spark.sql.SparkSession
import java.util.TimeZone

object S83_MovieLensAnalyticsSQL {

  def main(args: Array[String]): Unit = {

    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))

    val spark = SparkBuilder.build("MovieLensAnalyticsSQL")

    import spark.implicits._

    val moviesPath  = "s3a://datalake/silver/movies/"
    val ratingsPath = "s3a://datalake/silver/ratings/"

    val goldPath = "s3a://datalake/gold/sql_movielens/"

    // -------------------------
    // Read silver
    // -------------------------

    val moviesDf =
      spark.read.parquet(moviesPath)

    val ratingsDf =
      spark.read.parquet(ratingsPath)

    // temp tables

    moviesDf.createOrReplaceTempView("movies")
    ratingsDf.createOrReplaceTempView("ratings")

    // -------------------------
    // CACHE tables
    // -------------------------

    spark.sql("CACHE TABLE movies")
    spark.sql("CACHE TABLE ratings")

    spark.sql("SELECT * FROM movies LIMIT 5").show(false)
    spark.sql("SELECT * FROM ratings LIMIT 5").show(false)


    // -------------------------
    // rating analytics
    // -------------------------

    spark.sql(
      """
        CREATE OR REPLACE TEMP VIEW rating_agg AS
        SELECT
          movieId,
          COUNT(*) AS rating_count,
          AVG(rating) AS avg_rating
        FROM ratings
        GROUP BY movieId
      """
    )

    spark.sql("CACHE TABLE rating_agg")

    spark.sql("SELECT * FROM rating_agg LIMIT 5")
      .show(false)


    // -------------------------
    // join with broadcast
    // -------------------------

    spark.sql(
      """
    CREATE OR REPLACE TEMP VIEW analytics AS
    SELECT /*+ BROADCAST(m) */
      r.movieId,
      m.title,
      m.genres,
      r.rating_count,
      r.avg_rating
    FROM rating_agg r
    JOIN movies m
    ON r.movieId = m.movieId
  """
    )

    spark.sql("CACHE TABLE analytics")

    spark.sql("SELECT * FROM analytics LIMIT 5")
      .show(false)


    // -------------------------
    // write GOLD parquet
    // -------------------------

    val analyticsDf =
      spark.table("analytics")

    val goldAnalyticsPath =
      goldPath + "sql_movie_analytics/"

    analyticsDf.write
      .mode("overwrite")
      .parquet(goldAnalyticsPath)

    println("Gold written: " + goldAnalyticsPath)


    // =========================
    // JDBC
    // =========================

    val mysqlUrl =
      "jdbc:mysql://mysql:3306/ecomm?serverTimezone=UTC"

    val mysqlProps = new java.util.Properties()
    mysqlProps.setProperty("user", "root")
    mysqlProps.setProperty("password", "root")
    mysqlProps.setProperty(
      "driver",
      "com.mysql.cj.jdbc.Driver"
    )

    spark.table("analytics")
      .write
      .mode("overwrite")
      .jdbc(
        mysqlUrl,
        "sql_movie_analytics",
        mysqlProps
      )

    println("Written MySQL sql_movie_analytics")


    val pgUrl =
      "jdbc:postgresql://postgres:5432/postgres?options=-c%20timezone=UTC"

    val pgProps = new java.util.Properties()
    pgProps.setProperty("user", "postgres")
    pgProps.setProperty("password", "postgres")
    pgProps.setProperty(
      "driver",
      "org.postgresql.Driver"
    )

    spark.table("analytics")
      .write
      .mode("overwrite")
      .jdbc(
        pgUrl,
        "public.sql_movie_analytics",
        pgProps
      )

    println("Written Postgres sql_movie_analytics")


    // -------------------------
    // Top movies
    // -------------------------

    spark.sql(
      """
        CREATE OR REPLACE TEMP VIEW top_movies AS
        SELECT *
        FROM analytics
        WHERE rating_count >= 100
          AND avg_rating >= 4.0
        ORDER BY avg_rating DESC
        LIMIT 10
      """
    )

    spark.sql("CACHE TABLE top_movies")

    spark.sql("SELECT * FROM top_movies")
      .show(false)


    val goldTopPath =
      goldPath + "sql_most_popular_movies/"

    spark.table("top_movies")
      .write
      .mode("overwrite")
      .parquet(goldTopPath)

    println("Gold written: " + goldTopPath)


    spark.table("top_movies")
      .write
      .mode("overwrite")
      .jdbc(
        mysqlUrl,
        "sql_most_popular_movies",
        mysqlProps
      )

    spark.table("top_movies")
      .write
      .mode("overwrite")
      .jdbc(
        pgUrl,
        "public.sql_most_popular_movies",
        pgProps
      )

    println("Top movies written to JDBC")

    spark.stop()
  }
}