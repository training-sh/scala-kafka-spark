package spark.workshop

import org.apache.spark.sql.SparkSession
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import scala.util.matching.Regex
import scala.util.Try

object S53_LogAnalysis {

  case class ApacheLog(
                        ip: String,
                        time: ZonedDateTime,
                        method: String,
                        endpoint: String,
                        protocol: String,
                        status: Int,
                        bytes: Long,
                        referrer: String,
                        agent: String
                      )

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("ApacheLogAnalysis")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext
    sc.setLogLevel("WARN")

    val logs = sc.textFile("data/apache_logs.txt")

    // Correct Apache combined log regex
    val logPattern: Regex =
      """^(\S+) \S+ \S+ \[(.*?)\] "(\S+) (\S+) (\S+)" (\d{3}) (\d+) "(.*?)" "(.*?)"$""".r


    // Step 1 : Parse logs -> Tuple
    val parsedTuples = logs.flatMap {

      case logPattern(ip, time, method, endpoint, protocol, status, bytes, referrer, agent) =>
        Some((ip, time, method, endpoint, protocol, status.toInt, bytes.toLong, referrer, agent))

      case _ =>
        None
    }

    println("Parsed Tuple Sample")
    parsedTuples.take(5).foreach(println)


    // Step 2 : Tuple -> Case Class
    val parsedLogs = parsedTuples.mapPartitions { iter =>

      val formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z")

      iter.map {
        case (ip, time, method, endpoint, protocol, status, bytes, referrer, agent) =>

          val parsedTime =
            Try(ZonedDateTime.parse(time, formatter)).getOrElse(null)

          ApacheLog(
            ip,
            parsedTime,
            method,
            endpoint,
            protocol,
            status,
            bytes,
            referrer,
            agent
          )
      }
    }

    println("\nCase Class Sample")
    parsedLogs.take(5).foreach(println)


    // HTTP Method counts
    val methodCounts =
      parsedLogs.map(log => (log.method, 1)).reduceByKey(_ + _)

    println("\nHTTP Method Counts")
    methodCounts.collect().foreach(println)


    // Status codes
    val statusCounts =
      parsedLogs.map(log => (log.status, 1)).reduceByKey(_ + _)

    println("\nStatus Code Counts")
    statusCounts.collect().foreach(println)


    // Client errors
    val clientErrors =
      parsedLogs.filter(log => log.status >= 400 && log.status < 500).count()

    println("\nClient Errors (4xx): " + clientErrors)


    // Server errors
    val serverErrors =
      parsedLogs.filter(log => log.status >= 500).count()

    println("\nServer Errors (5xx): " + serverErrors)


    // Unique IPs
    val uniqueIPs =
      parsedLogs.map(_.ip).distinct().count()

    println("\nUnique IPs: " + uniqueIPs)


    // Top IPs
    val topIPs =
      parsedLogs.map(log => (log.ip, 1))
        .reduceByKey(_ + _)
        .sortBy(_._2, ascending = false)
        .take(10)

    println("\nTop IPs")
    topIPs.foreach(println)


    // Top endpoints
    val topEndpoints =
      parsedLogs.map(log => (log.endpoint, 1))
        .reduceByKey(_ + _)
        .sortBy(_._2, ascending = false)
        .take(10)

    println("\nTop Endpoints")
    topEndpoints.foreach(println)


    // Requests per hour
    val hourlyRequests =
      parsedLogs
        .filter(_.time != null)
        .map(log => (log.time.getHour, 1))
        .reduceByKey(_ + _)
        .sortByKey()

    println("\nRequests Per Hour")
    hourlyRequests.collect().foreach(println)

    spark.stop()

  }
}