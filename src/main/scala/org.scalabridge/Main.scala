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
    //set tokens for twitter4s
    val logger = Logger("logger")
    val consumerToken = ConsumerToken(key = "wVEpDxPf1XRmvYmrZHgvULaGM", secret = "7q9Z1DuogwyZtdeYkkgxzkmIW8qTcfJRH0x9IXUFNw5gmJtNEs")
    val accessToken = AccessToken(key = "3317308574-mgg8AWsjQoLhReURzpIZ3htV9t3dMfsNNEBcvBF", secret = "GOyWnhDoq8imihe47y2E6BsTrLvOxQjz3P2HKlAZkHdrA")
    val restClient = TwitterRestClient(consumerToken, accessToken)
    val r = scala.util.Random
    implicit val ec = ExecutionContext.global
    logger.info(s"Application starting...")

    //building blocks for the images
    def nextColor(color: Color): Random[Color] = {
      val spin = Random.normal(15.0, 10.0)
      spin map { s => color.spin(Angle(s)) }
    }

    def coloredCircle(color: Color, size: Double): Image =
      Image.circle(size).strokeWidth(1.0).strokeColor(color.spin(Angle(180))).fillColor(color)

    //create an image based on day of week
    def dayImage(count: Int, color: Color, size: Double, dayOfWeek: DayOfWeek): Random[Image] = {
      count match {
        case 0 => Random.always(Image.empty)
        case n =>
          val loop = coloredCircle(color, size)
          val loops = nextColor(color) flatMap { c => dayImage(n - 1, c, size + r.nextInt(7), dayOfWeek) }
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

    //convert day of week string to day of week ADT
    def dayToString(today: String): DayOfWeek =
    today match {
      case "Sunday" => DayOfWeek.SUNDAY
      case "Monday" => DayOfWeek.MONDAY
      case "Tuesday" => DayOfWeek.TUESDAY
      case "Wednesday" => DayOfWeek.WEDNESDAY
      case "Thursday" => DayOfWeek.THURSDAY
      case "Friday" => DayOfWeek.FRIDAY
      case "Saturday" => DayOfWeek.SATURDAY
    }

    logger.info(s"Variables initialized.")

    //get the current day of the week
    val now = Calendar.getInstance()
    val currentDay = now.get(Calendar.DAY_OF_WEEK)
    val weekDays = Seq("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    val today = weekDays(currentDay-1)
    logger.info(s"Getting the current day of the week from Calendar instance. Current day is: $currentDay; today is $today")

    //generate the image, write to file, and queue up the tweet
    val t = new java.util.Timer()
    val task = new java.util.TimerTask {
      def run() = {
        case object fileName {
          val id = randomUUID().toString
          val newName = id.concat(".png")
        }
        val filePath = "/tmp/" + fileName.newName
        val imageForToday = dayToString(today)
        dayImage(50, nextColor(Color.crimson).run, r.nextInt(10), imageForToday).run.write[Png](filePath)
        val status = "Happy " + today + "!"
        logger.info(s"Created tweet with return code $restClient, status: $status, file path: $filePath, day: $imageForToday")
        for {
          upload <- restClient.uploadMediaFromPath(filePath)
          tweet <- restClient.createTweet(status, media_ids = Seq(upload.media_id))
        } yield tweet
      }
    }

    //set the timer for the tweet task
    now.set(Calendar.HOUR_OF_DAY, 11)
    now.set(Calendar.MINUTE, 0)
    now.set(Calendar.SECOND, 0)
    logger.info(s"Setting the timer for the tweet task: time ${now.getTime}")
    logger.debug(s"Time zone for the scheduled tweet task is: ${now.getTimeZone}")

    //schedule the tweet and repeat
    val repeatInterval = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
    t.schedule(task,now.getTime(), repeatInterval)
    logger.info(s"Next tweet is scheduled to run in $repeatInterval")


  }


}
