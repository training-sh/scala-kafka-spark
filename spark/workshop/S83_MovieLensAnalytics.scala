package spark.workshop


package spark.workshop

import org.apache.spark.sql.{SparkSession, functions => F}
import java.util.TimeZone

/*

/opt/spark/bin/spark-submit \
  --class spark.workshop.S83_MovieLensAnalytics \
  --master spark://spark-master:7077 \
  /jars/scala-kafka-spark-workshop_2.12-0.1.0-SNAPSHOT.jar

*/

object S83_MovieLensAnalytics {

  def main(args: Array[String]): Unit = {

    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))

    val spark = SparkBuilder.build("MovieLensAnalytics")

    import spark.implicits._

    val moviesPath = "s3a://datalake/silver/movies/"
    val ratingsPath = "s3a://datalake/silver/ratings/"

    val goldPath = "s3a://datalake/gold/movielens/"

    // -------------------------
    // Read silver
    // -------------------------

    val moviesDf =
      spark.read.parquet(moviesPath)

    // rating from silver zone
    val ratingsDf =
      spark.read.parquet(ratingsPath)

    println("Movies")
    moviesDf.show(5, false)

    println("Ratings")
    ratingsDf.show(5, false)


    // -------------------------
    // Analytics
    // avg rating per movie
    // -------------------------

    val ratingAggDf =
      ratingsDf
        .groupBy($"movieId")
        .agg(
          F.count("*").as("rating_count"),
          F.avg("rating").as("avg_rating")
        )

    println("Rating analytics")
    ratingAggDf.show(5, false)


    // -------------------------
    // Join movies + ratings
    // -------------------------
    import org.apache.spark.sql.functions.broadcast

    // broadcast join
    val analyticsDf =
      ratingAggDf.join(
        broadcast(moviesDf), //broadcast
        Seq("movieId"),
        "inner"
      )

    println("Joined with movies")

    analyticsDf.show(5, false)

//    analyticsDf.cache()
//    analyticsDf.count()
//
    analyticsDf.show(5, false)

//    val analyticsDf =
//      joinedDf
//        .groupBy(
//          $"movieId",
//          $"title",
//          $"genres"
//        )
//        .agg(
//          F.count("*").as("rating_count"),
//          F.avg("rating").as("avg_rating")
//        )
//        .orderBy(F.desc("avg_rating"))


//      import org.apache.spark.sql.functions.broadcast
    ////
    ////    val topMoviesBroadcast =
    ////      broadcast(topMoviesDf)
    ////
    // reuse analyticsDf multiple times
    // analyticsDf.cache()

    // println("Cached analyticsDf")
    // analyticsDf.count()

    println("Analytics")
    analyticsDf.show(20, false)


    // -------------------------
    // Write GOLD parquet
    // -------------------------

    analyticsDf.write
      .mode("overwrite")
      .parquet(goldPath)

    println("Gold written to: " + goldPath)


    // =========================
    // MYSQL
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

    analyticsDf.write
      .mode("overwrite")
      .jdbc(
        mysqlUrl,
        "movie_analytics",
        mysqlProps
      )

    println("Written to MySQL")


    // =========================
    // POSTGRES
    // =========================

    val pgUrl =
      "jdbc:postgresql://postgres:5432/postgres?options=-c%20timezone=UTC"

    val pgProps = new java.util.Properties()
    pgProps.setProperty("user", "postgres")
    pgProps.setProperty("password", "postgres")
    pgProps.setProperty(
      "driver",
      "org.postgresql.Driver"
    )

    analyticsDf.write
      .mode("overwrite")
      .jdbc(
        pgUrl,
        "public.movie_analytics",
        pgProps
      )

    println("Written to PostgreSQL")


    val topMoviesDf =
      analyticsDf
        .filter(
          F.col("rating_count") >= 100 &&
            F.col("avg_rating") >= 4.0
        )
        .orderBy(F.desc("avg_rating"))
        .limit(10)

    println("Top Movies")
    topMoviesDf.show(false)


    val goldPopularPath =
      "s3a://datalake/gold/most-popular-movies/"

    topMoviesDf.write
      .mode("overwrite")
      .parquet(goldPopularPath)

    println("Written to gold most-popular-movies")



    topMoviesDf.write
      .mode("overwrite")
      .jdbc(
        mysqlUrl,
        "most_popular_movies",
        mysqlProps
      )

    println("Written to MySQL most_popular_movies")



    topMoviesDf.write
      .mode("overwrite")
      .jdbc(
        pgUrl,
        "public.most_popular_movies",
        pgProps
      )

    println("Written to PostgreSQL most_popular_movies")

    spark.stop()
  }
}