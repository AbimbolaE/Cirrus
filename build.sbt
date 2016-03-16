name := "cirrus"

version := "0.0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "io.spray" %%  "spray-json" % "1.3.2" exclude("org.scala-lang", "scala-library")
//  "io.spray" %%  "spray-json" % "1.3.2" % "provided" exclude("org.scala-lang", "scala-library")
)

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-library" % "2.11.8" % "provided"
)
