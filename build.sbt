import ReleaseTransformations._
import sbt._

val theScalaVersion = "2.12.6"

name := "scalajs-monaco-editor"

scalaVersion := theScalaVersion

organization := "com.github.benhutchison"

crossScalaVersions := Seq(theScalaVersion)

libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.6",
    "org.specs2" %%% "specs2-core" % "4.3.5-78abffa2e-20181150936" % "test",
)

jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()

publishMavenStyle := true
licenses += ("The Apache Software License, Version 2.0", url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
homepage := Some(url("https://github.com/benhutchison/scalajs-monaco-editor"))
developers := List(Developer("benhutchison", "Ben Hutchison", "brhutchison@gmail.com", url = url("https://github.com/benhutchison")))
scmInfo := Some(ScmInfo(url("https://github.com/benhutchison/scalajs-monaco-editor"), "scm:git:https://github.com/benhutchison/scalajs-monaco-editor.git"))
releaseCrossBuild := true
releasePublishArtifactsAction := PgpKeys.publishSigned.value
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  publishArtifacts,
  setNextVersion,
  commitNextVersion,
)

enablePlugins(ScalaJSPlugin)

ThisBuild / publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)