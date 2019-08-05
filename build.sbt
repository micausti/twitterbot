val scalabridge = Project("scalabridge", file("."))
  .settings(
    name := "scalabridge-starter",
    organization := "org.scalabridge",
    version := "0.1.0",
    scalaVersion := "2.12.8",
    initialCommands in console := """
      |import cats.instances.all._
      |import doodle.java2d._
      |import doodle.syntax._
      |import doodle.effect.Writer._
      |import doodle.examples._
      |import doodle.image._
      |import doodle.image.syntax._
      |import doodle.image.examples._
      |import doodle.animate.syntax._
      |import doodle.animate.java2d._
      |import doodle.animate.examples._
      |import doodle.explore.syntax._
      |import doodle.explore.java2d._
      |import doodle.explore.java2d.examples
      |import doodle.core._
      |import doodle.algebra.Image._
      |import java.io.File._
      |import java.util.Timer
      |import doodle.image.syntax._
      |import org.apache.commons.codec.binary
      |import scala.io.Source
      |import org.json4s.JsonDSL._
     """.trim.stripMargin,

    resolvers  ++= Seq (Resolver.sonatypeRepo("releases"),
      "Artima Maven Repository" at "http://repo.artima.com/releases"),
    
    
    libraryDependencies ++= Seq(
      // The library "cats" provides useful abstractions and utilities for doing functional programming in Scala
      "org.typelevel" %% "cats-core" % "1.6.0",
      // Scalatest is a testing framework for Scala
      "org.scalatest" %% "scalatest" % "3.0.6" % "test",
      //Doodle
      "org.creativescala" %% "doodle" % "0.9.3",
      //ApacheCommons Codec
      "commons-codec" % "commons-codec" % "1.12",
      //JSON4S
      "org.json4s" %% "json4s-native" % "3.6.6",
      "com.danielasfregola" %% "twitter4s" % "6.1",
      //scala-logging
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "org.scalactic" %% "scalactic" % "3.0.8",
      "com.typesafe" % "config" % "1.3.2",
      
    ),
    // Some compiler flags which are good defaults
    scalacOptions ++= Seq(
      "-deprecation", // Emit warning and location for usages of deprecated APIs.
      "-encoding",
      "utf-8", // Specify character encoding used by source files.
      "-explaintypes", // Explain type errors in more detail.
      "-feature", // Emit warning and location for usages of features that should be imported explicitly.
      "-unchecked", // Enable additional warnings where generated code depends on assumptions.
      "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
      "-Xfuture", // Turn on future language features.
      "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
      "-Xlint:by-name-right-associative", // By-name parameter of right associative operator.
      "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
      "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
      "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
      "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
      "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
      "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
      "-Xlint:nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
      "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
      "-Xlint:option-implicit", // Option.apply used implicit view.
      "-Xlint:package-object-classes", // Class or object defined in package object.
      "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
      "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
      "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
      "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
      "-Xlint:unsound-match", // Pattern match may not be typesafe.
      "-Yno-adapted-args", // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
      "-Ypartial-unification", // Enable partial unification in type constructor inference
      "-Ywarn-dead-code", // Warn when dead code is identified.
      "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
      "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
      "-Ywarn-infer-any", // Warn when a type argument is inferred to be `Any`.
      "-Ywarn-nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
      "-Ywarn-nullary-unit", // Warn when nullary methods return Unit.
      "-Ywarn-numeric-widen", // Warn when numerics are widened.
      "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused.
      "-Ywarn-unused:locals", // Warn if a local definition is unused.
      "-Ywarn-unused:params", // Warn if a value parameter is unused.
      "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused.
      "-Ywarn-unused:privates", // Warn if a private member is unused.
      "-Ywarn-value-discard" // Warn when non-Unit expression results are unused.
    )
  )
