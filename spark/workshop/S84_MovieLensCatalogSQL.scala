package spark.workshop

import java.util.TimeZone

object S84_MovieLensCatalogSQL {

  def main(args: Array[String]): Unit = {

    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))

    val spark =
      SparkBuilder.build("MovieLensCatalogSQL")

    // -------------------------
    // paths
    // -------------------------

    val bronzeMovies =
      "s3a://datalake/bronze/movies/"

    val bronzeRatings =
      "s3a://datalake/bronze/ratings/"

    val silverMovies =
      "s3a://datalake/silver/movies_parquet/"

    val silverRatings =
      "s3a://datalake/silver/ratings_orc/"

    val goldAnalytics =
      "s3a://datalake/gold/movie_analytics/"

    val goldTop =
      "s3a://datalake/gold/top_movies/"


    // =========================
    // CREATE NAMESPACE
    // =========================

    spark.sql(
      """
      CREATE DATABASE IF NOT EXISTS movielens
      """
    )

    spark.sql("USE movielens")


    // =========================
    // BRONZE EXTERNAL TABLES
    // =========================

    spark.sql("DROP TABLE IF EXISTS bronze_movies_ext")

    spark.sql(
      s"""
      CREATE TABLE bronze_movies_ext (
        movieId INT,
        title STRING,
        genres STRING
      )
      USING csv
      OPTIONS (
        path '$bronzeMovies',
        header 'true'
      )
      """
    )


    spark.sql("DROP TABLE IF EXISTS bronze_ratings_ext")

    spark.sql(
      s"""
      CREATE TABLE bronze_ratings_ext (
        userId INT,
        movieId INT,
        rating DOUBLE,
        timestamp LONG
      )
      USING csv
      OPTIONS (
        path '$bronzeRatings',
        header 'true'
      )
      """
    )


    println("---------bronze movies----------------")
    spark.sql("SELECT * FROM bronze_movies_ext LIMIT 5").show(false)

    println("---------bronze ratings----------------")
    spark.sql("SELECT * FROM bronze_ratings_ext LIMIT 5").show(false)


    // =========================
    // SILVER CTAS (PARQUET)
    // =========================

    spark.sql("DROP TABLE IF EXISTS silver_movies_ext")

    spark.sql(
      s"""
      CREATE TABLE silver_movies_ext
      USING parquet
      LOCATION '$silverMovies'
      AS
      SELECT
        movieId,
        title,
        genres,
        current_timestamp() AS ingest_time
      FROM bronze_movies_ext
      """
    )


    // =========================
    // SILVER CTAS (ORC)
    // =========================

    spark.sql("DROP TABLE IF EXISTS silver_ratings_ext")

    spark.sql(
      s"""
      CREATE TABLE silver_ratings_ext
      USING orc
      LOCATION '$silverRatings'
      AS
      SELECT
        userId,
        movieId,
        rating,
        to_timestamp(
          from_unixtime(timestamp)
        ) AS rating_time,
        current_timestamp() AS ingest_time
      FROM bronze_ratings_ext
      """
    )


    println("---------silver movies ----------------")
    spark.sql("SELECT * FROM silver_movies_ext LIMIT 5").show(false)

    println("---------silver ratings----------------")
    spark.sql("SELECT * FROM silver_ratings_ext LIMIT 5").show(false)


    // =========================
    // ANALYTICS → GOLD
    // =========================

    spark.sql("DROP TABLE IF EXISTS gold_movie_analytics")

    spark.sql(
      s"""
      CREATE TABLE gold_movie_analytics
      USING parquet
      LOCATION '$goldAnalytics'
      AS
      SELECT
        r.movieId,
        m.title,
        m.genres,
        COUNT(*) AS rating_count,
        AVG(r.rating) AS avg_rating
      FROM silver_ratings_ext r
      JOIN silver_movies_ext m
      ON r.movieId = m.movieId
      GROUP BY
        r.movieId,
        m.title,
        m.genres
      """
    )

    println("---------gold gold_movie_analytics----------------")


    spark.sql(
      "SELECT * FROM gold_movie_analytics LIMIT 5"
    ).show(false)


    // =========================
    // TOP MOVIES
    // =========================

    spark.sql("DROP TABLE IF EXISTS gold_top_movies")

    spark.sql(
      s"""
      CREATE TABLE gold_top_movies
      USING parquet
      LOCATION '$goldTop'
      AS
      SELECT *
      FROM gold_movie_analytics
      WHERE rating_count >= 100
        AND avg_rating >= 4.0
      ORDER BY avg_rating DESC
      LIMIT 10
      """
    )

    println("---------gold gold_movie_analytics----------------")

    spark.sql(
      "SELECT * FROM gold_top_movies"
    ).show(false)


    // =========================
    // SHOW TABLES
    // =========================

    println("---------all tables----------------")
    spark.sql("SHOW TABLES").show(false)

    spark.stop()
  }

}