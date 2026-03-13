ThisBuild / version := "0.1.0-SNAPSHOT"


// 2.12.21

ThisBuild / scalaVersion := "2.12.18"

// confluent publish their libraries in their own maven repo
resolvers += "Confluent" at "https://packages.confluent.io/maven/"

val confluentVersion = "7.6.0"
val kafkaVersion = "3.4.1"

val avroVersion = "1.11.3"
val sparkVersion = "3.5.3"


//val kafkaVersion = "3.7.0"


val hadoopVersion = "3.3.4" // "3.3.6"
val awsSdkVersion = "1.12.661"

val icebergVersion = "1.5.2"

// iceberg-spark-runtime-3.5
// % "provided"   this to be added if we run in cluster, thursday

lazy val root = (project in file("."))
  .settings(
    name := "scala-kafka-spark-workshop",
    libraryDependencies ++= Seq(
      "org.apache.kafka" % "kafka-clients" % kafkaVersion,
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.17.0",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.17.0",
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % "2.15.2",

      //FIXME: Investigate
     // "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.6",

      "org.apache.avro" % "avro-compiler" % avroVersion,
      "org.apache.avro" % "avro" % avroVersion,

      "io.confluent" % "kafka-avro-serializer" % confluentVersion,
      "io.confluent" % "kafka-streams-avro-serde" % confluentVersion,

      // RocksDB (state store)
      "org.rocksdb" % "rocksdbjni" % "8.10.0",

      // Kafka Streams
      "org.apache.kafka" % "kafka-streams" % kafkaVersion,

      "org.slf4j" % "slf4j-simple" % "2.0.9",

      "org.apache.spark" %% "spark-core" % sparkVersion,
      "org.apache.spark" %% "spark-sql" % sparkVersion,

      "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion,


      "org.apache.hadoop" % "hadoop-aws" % hadoopVersion,
      "com.amazonaws" % "aws-java-sdk-bundle" % awsSdkVersion,

      "org.apache.iceberg" %% "iceberg-spark-runtime-3.5" % icebergVersion,
      "org.apache.iceberg" % "iceberg-aws" % icebergVersion,

      "org.postgresql" % "postgresql" % "42.7.3",
      "com.mysql" % "mysql-connector-j" % "8.4.0"

    )
  )

import java.nio.file.{Files, Paths, StandardCopyOption}
// sbt package
Compile / packageBin := {
  val jar = (Compile / packageBin).value   // build the jar first

  val destDir = Paths.get("C:/training/jars")
  val destFile = destDir.resolve(jar.getName)

  Files.createDirectories(destDir)

  Files.copy(
    jar.toPath,
    destFile,
    StandardCopyOption.REPLACE_EXISTING
  )

  println(s"Copied jar to: $destFile")

  jar
}