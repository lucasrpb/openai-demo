ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.16"

lazy val root = (project in file("."))
  .settings(
    name := "openai-demo"
  )

libraryDependencies ++= Seq(
  "com.openai" % "openai-java" % "3.0.2",
  "com.sun.mail" % "javax.mail" % "1.6.2"
)