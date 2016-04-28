name := "cirrus"

version := "1.4.1"

organization := "com.github.godis"

scalaVersion := "2.11.8"

fork in Test := true

scalacOptions in Test ++= Seq("-Yrangepos", "-feature")

conflictManager := ConflictManager.strict

libraryDependencies ++= Seq(
  "io.argonaut" %% "argonaut" % "6.1" % "provided",
  "io.spray" %%  "spray-json" % "1.3.2" % "provided",
  "com.typesafe.play" %% "play-json" % "2.5.1" % "provided",

  "org.specs2" %% "specs2-core" % "3.6.5" % Test,
  "com.github.tomakehurst" % "wiremock" % "1.57" % Test
)

dependencyOverrides ++= Set(
  "org.scalaz" %% "scalaz-core" % "7.1.1",
  "org.scala-lang" % "scala-library" % "2.11.8",
  "org.scala-lang" % "scala-reflect" % "2.11.8",
  "org.scala-lang" % "scala-compiler" % "2.11.8",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.7.1",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.7.1",
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.7.1"
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
