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

    def dayOfWeek (now: Calendar): Int = {
       now.get(Calendar.DAY_OF_WEEK)
    }

    def today (dayOfWeek: Int): String = {
      dayOfWeek match {
        case 1 => "Sunday"
        case 2 => "Monday"
        case 3 => "Tuesday"
        case 4 => "Wednesday"
        case 5 => "Thursday"
        case 6 => "Friday"
        case 7 => "Saturday"
      }
    }

    case object Date {
      //set vals for date properties
      val weekDays = Seq("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
      val now = Calendar.getInstance()
      val currentDay = now.get(Calendar.DAY_OF_WEEK)
      val today = Date.weekDays(Date.currentDay-1)
  }

    case object NewImage {
      //set vals for new image to generate
      val id = randomUUID().toString
      val newName = id.concat(".png")
      val filePath = "/tmp/" + newName
      val imageForToday = dayToString(Date.today)
      val status = "Happy " + Date.today + "!"
    }

    case object ScheduleTime {
      val repeatInterval = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
      val startTime = Date.now.getTime()
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


    def dayImage(count: Int = 50, color: Color  = nextColor(Color.crimson).run, size: Double = scala.util.Random.nextInt(10), dayOfWeek: DayOfWeek): Random[Image] = {
      //create an image based on day of week
      count match {
        case 0 => Random.always(Image.empty)
        case n =>
          val loop = coloredCircle(color, size)
          val loops = nextColor(color) flatMap { c => dayImage(n - 1, c, size + scala.util.Random.nextInt(7), dayOfWeek) }
          dayOfWeek match {
            case DayOfWeek.SUNDAY => loops map { l => (loop beside loop beside loop) above (loop beside loop beside loop) above (loop beside loop beside loop) on l }
            case DayOfWeek.MONDAY => loops map { l => loop above (loop beside loop beside loop) above loop on l }
            case DayOfWeek.TUESDAY => loops map { l => (loop beside loop) below loop on l }
            case DayOfWeek.WEDNESDAY => loops map { l => (loop beside loop beside loop beside loop beside loop) on l }
            case DayOfWeek.THURSDAY => loops map { l => loop above (loop beside loop beside loop) above (loop beside loop beside loop beside loop beside loop) above (loop beside loop beside loop) above loop above (loop beside loop beside loop) above (loop beside loop beside loop beside loop beside loop) above (loop beside loop beside loop) above loop on l }
            case DayOfWeek.FRIDAY => loops map { l => loop above (loop beside loop beside loop) above (loop beside loop beside loop beside loop beside loop) above (loop beside loop beside loop) above loop on l }
            case DayOfWeek.SATURDAY => loops map { l => loop above (loop beside loop beside loop) above (loop beside loop beside loop) above (loop beside loop beside loop beside loop beside loop) above (loop beside loop beside loop) above (loop beside loop beside loop) above loop on l }
          }
      }
    }

    def dayToString(today: String): DayOfWeek = {
      //convert day of week string to day of week ADT
      today match {
        case "Sunday" => DayOfWeek.SUNDAY
        case "Monday" => DayOfWeek.MONDAY
        case "Tuesday" => DayOfWeek.TUESDAY
        case "Wednesday" => DayOfWeek.WEDNESDAY
        case "Thursday" => DayOfWeek.THURSDAY
        case "Friday" => DayOfWeek.FRIDAY
        case "Saturday" => DayOfWeek.SATURDAY
      }
    }



/*    def createTodayImage(): Unit= {
      //create image and write to file
      dayImage(dayOfWeek = NewImage.imageForToday).run.write[Png](NewImage.filePath)
    }
*/
    val task: TimerTask = new java.util.TimerTask {
      //queue up the tweet
      def run() = {
        implicit val ec = ExecutionContext.global
        dayImage(dayOfWeek = NewImage.imageForToday).run.write[Png](NewImage.filePath)
        for {
          upload <- Twitter.restClient.uploadMediaFromPath(NewImage.filePath)
          tweet <- Twitter.restClient.createTweet(NewImage.status, media_ids = Seq(upload.media_id))
        } yield tweet
      }
    }

    def scheduleTweet(task: TimerTask): Unit = {
    //schedule the tweet and repeat
    val t = new java.util.Timer()
    t.schedule(task, Date.now.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))
  }

  }





