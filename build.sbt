name := "cirrus"

version := "0.0.1"

organization := "com.godis"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "io.argonaut" %% "argonaut" % "6.1" exclude("org.scala-lang", "scala-library"),
  "io.spray" %% "spray-json" % "1.3.2" exclude("org.scala-lang", "scala-library")
//  "io.argonaut" %% "argonaut" % "6.1" % "provided" exclude("org.scala-lang", "scala-library")
//  "io.spray" %%  "spray-json" % "1.3.2" % "provided" exclude("org.scala-lang", "scala-library")
)

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-library" % "2.11.8" % "provided"
)

offline := true
