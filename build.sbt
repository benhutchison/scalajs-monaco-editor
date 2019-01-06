import ReleaseTransformations._
import sbt._

val theScalaVersion = "2.12.6"
ThisBuild / name := "scalajs-monaco-editor"
ThisBuild / scalaVersion := theScalaVersion
ThisBuild / organization := "com.github.benhutchison"
ThisBuild / crossScalaVersions := Seq(theScalaVersion)

ThisBuild / jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()

lazy val root = project.in(file("."))
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.6",
    ),

    publishTo := Some(
      if (isSnapshot.value)
        Opts.resolver.sonatypeSnapshots
      else
        Opts.resolver.sonatypeStaging
    ),
    publishMavenStyle := true,
    licenses += ("The Apache Software License, Version 2.0", url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
    homepage := Some(url("https://github.com/benhutchison/scalajs-monaco-editor")),
    developers := List(Developer("benhutchison", "Ben Hutchison", "brhutchison@gmail.com", url = url("https://github.com/benhutchison"))),
    scmInfo := Some(ScmInfo(url("https://github.com/benhutchison/scalajs-monaco-editor"), "scm:git:https://github.com/benhutchison/scalajs-monaco-editor.git")),
    releaseCrossBuild := true,
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
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
    ),
  )

lazy val example = project
  .in(file("example"))
  .dependsOn(root)//.aggregate(root)
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(
    npmDependencies in Compile ++= List(
      "monaco-editor" -> "^0.15.6",
    ),
    webpackConfigFile := Some(baseDirectory.value / "webpack.config.js"),
    npmDevDependencies in Compile ++= List(
      "html-webpack-plugin" -> "3.0.6",
      "copy-webpack-plugin" -> "4.6.0",
      "webpack-merge" -> "4.1.2",
      "style-loader" -> "0.21.0",
      "css-loader" -> "0.28.11",
    ),
    scalaJSUseMainModuleInitializer := true
  )


