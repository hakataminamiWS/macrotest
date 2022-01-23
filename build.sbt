import Dependencies._

ThisBuild / scalaVersion := "2.13.7"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

lazy val input = "./src/main/scala/input/Input.scala"
lazy val output = "./src/main/scala/output/output.sql"
lazy val caseObjectToSqlEnum = taskKey[Unit]("caseObjectToSqlEnum")
lazy val macro_test = (project in file("macro_test"))
  .settings(
    libraryDependencies += "org.scalameta" %% "scalameta" % "4.4.33",
    caseObjectToSqlEnum := (Compile / runMain)
      .toTask(s" CaseObjectToSqlEnum ${input} ${output}")
      .value
  )

lazy val root = (project in file("."))
  .aggregate(macro_test)
  .settings(
    libraryDependencies += "com.beachape" %% "enumeratum" % "1.7.0",
    libraryDependencies += scalaTest % Test
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
