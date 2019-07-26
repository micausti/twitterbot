package org.scalabridge

import java.time.DayOfWeek
import java.util.Calendar
import java.util.UUID.randomUUID
import java.util.concurrent.TimeUnit

import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.typesafe.scalalogging.Logger
import doodle.core.{Angle, Color}
import doodle.effect.Writer.Png
import doodle.image.Image
import doodle.random.Random

import scala.concurrent.ExecutionContext
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

class MyProcess {

    case object Twitter {
      //set tokens for twitter4s
      val consumerToken = ConsumerToken(key = "wVEpDxPf1XRmvYmrZHgvULaGM", secret = "7q9Z1DuogwyZtdeYkkgxzkmIW8qTcfJRH0x9IXUFNw5gmJtNEs")
      val accessToken = AccessToken(key = "3317308574-mgg8AWsjQoLhReURzpIZ3htV9t3dMfsNNEBcvBF", secret = "GOyWnhDoq8imihe47y2E6BsTrLvOxQjz3P2HKlAZkHdrA")
      val restClient = TwitterRestClient(consumerToken, accessToken)
    }

    def getNumOfDay (now: Calendar): Int = {
       now.get(Calendar.DAY_OF_WEEK)
    }

    def nextColor(color: Color): Random[Color] = {
      //building blocks for the imageS, selects the next color to be used
      val spin = Random.normal(15.0, 10.0)
      spin map { s => color.spin(Angle(s)) }
    }

    def coloredCircle(color: Color, size: Double): Image = {
      //building blocks for images, generates a colored circle
      Image.circle(size).strokeWidth(1.0).strokeColor(color.spin(Angle(180))).fillColor(color)
    }

    def today (numOfDay: Int): String = {
      numOfDay match {
        case 1 => "Sunday"
        case 2 => "Monday"
        case 3 => "Tuesday"
        case 4 => "Wednesday"
        case 5 => "Thursday"
        case 6 => "Friday"
        case 7 => "Saturday"
      }
    }

  def dayImage(count: Int, color: Color, size: Double, dayOfWeek: Int): Random[Image] = {
    //create an image based on day of week
    count match {
      case 0 => Random.always(Image.empty)
      case n =>
        val loop = coloredCircle(color, size)
        val loops = nextColor(color) flatMap { c => dayImage(n - 1, c, size + scala.util.Random.nextInt(7), dayOfWeek) }
        dayOfWeek match {
          case 1 => loops map { l => (loop beside loop beside loop) above (loop beside loop beside loop) above (loop beside loop beside loop) on l }
          case 2 => loops map { l => loop above (loop beside loop beside loop) above loop on l }
          case 3 => loops map { l => (loop beside loop) below loop on l }
          case 4 => loops map { l => (loop beside loop beside loop beside loop beside loop) on l }
          case 5 => loops map { l => loop above (loop beside loop beside loop) above (loop beside loop beside loop beside loop beside loop) above (loop beside loop beside loop) above loop above (loop beside loop beside loop) above (loop beside loop beside loop beside loop beside loop) above (loop beside loop beside loop) above loop on l }
          case 6 => loops map { l => loop above (loop beside loop beside loop) above (loop beside loop beside loop beside loop beside loop) above (loop beside loop beside loop) above loop on l }
          case 7 => loops map { l => loop above (loop beside loop beside loop) above (loop beside loop beside loop) above (loop beside loop beside loop beside loop beside loop) above (loop beside loop beside loop) above (loop beside loop beside loop) above loop on l }
        }
    }
  }


  def filePath(): String = {
    "/tmp/" + randomUUID().toString + ".png"
  }

  def status(today: String): String = {
  "Happy " + today + "!"
  }

/*
    val task: TimerTask = new java.util.TimerTask {
      //queue up the tweet
      def run() = {
        implicit val ec = ExecutionContext.global
        for {
          upload <- Twitter.restClient.uploadMediaFromPath(NewImage.filePath)
          tweet <- Twitter.restClient.createTweet(NewImage.status, media_ids = Seq(upload.media_id))
        } yield tweet
      }
    }
*/
    def scheduleTweet(task: TimerTask, now: Calendar): Unit = {
    //schedule the tweet and repeat
    val t = new java.util.Timer()
    t.schedule(task, now.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))
  }

  }





