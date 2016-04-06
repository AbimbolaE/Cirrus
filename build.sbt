name := "cirrus"

version := "0.0.2-SNAPSHOT"

organization := "com.github.godis"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
//  "io.argonaut" %% "argonaut" % "6.1" exclude("org.scala-lang", "scala-library"),
//  "io.spray" %% "spray-json" % "1.3.2" exclude("org.scala-lang", "scala-library")
  "io.argonaut" %% "argonaut" % "6.1" % "provided" exclude("org.scala-lang", "scala-library"),
  "io.spray" %%  "spray-json" % "1.3.2" % "provided" exclude("org.scala-lang", "scala-library")
)

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-library" % "2.11.8" % "provided"
)

publishMavenStyle := true

publishArtifact in Test := false

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

pomIncludeRepository := { _ => false }

pomExtra :=
  <url>https://github.com/Godis/Cirrus</url>
  <licenses>
    <license>
      <name>Apache License, Version 2.0</name>
      <url>https://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
      <comments>A business-friendly OSS license</comments>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:Godis/Cirrus.git</url>
    <connection>scm:git:git@github.com:Godis/Cirrus.git</connection>
  </scm>
  <developers>
    <developer>
      <id>abimbolae</id>
      <name>Abimbola Esuruoso</name>
      <email>abimbolaesuruoso@gmail.com</email>
    </developer>
  </developers>
