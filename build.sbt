name := """minimal-akka-scala-seed"""

version := "1.0"
scalaVersion := "2.12.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.14"
)

fork in run := true
