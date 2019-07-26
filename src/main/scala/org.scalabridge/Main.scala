package org.scalabridge
import Utils.addThenDoubleIt
import doodle.image._
import doodle.image.syntax._
import doodle.core._
import doodle.java2d._
import doodle.effect.Writer._
import doodle.java2d.effect.Frame
import doodle.random.Random
import org.apache.commons.codec.binary.Base64
import org.json4s._
import org.json4s.JsonDSL._

import scala.io.{BufferedSource, Source}
import java.nio.file.{Files, Paths}
import java.time.DayOfWeek
import java.util.{Calendar, TimeZone, Timer, TimerTask}
import java.util.UUID.randomUUID
import java.util.concurrent.TimeUnit

import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}

import scala.concurrent.ExecutionContext
import com.typesafe.scalalogging._


object Main {

  def main(args: Array[String]): Unit = {

    val createTweet  = new MyProcess

    val now = Calendar.getInstance()
    val today = createTweet.today(createTweet.dayOfWeek(now))
    println(today)

 }

}
