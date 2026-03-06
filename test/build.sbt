ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.21"

lazy val root = (project in file("."))
  .settings(
    name := "scala-kafka-spark-workshop",
    libraryDependencies ++= Seq(
      "org.apache.kafka" % "kafka-clients" % "3.7.0"
    )
  )
