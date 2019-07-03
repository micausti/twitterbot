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
import java.util.{Timer, TimerTask}
import java.util.Calendar
import java.util.UUID.randomUUID

import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}

import scala.concurrent.ExecutionContext




object Main {



  def main(args: Array[String]): Unit = {
    //set tokens for twitter4s
    val consumerToken = ConsumerToken(key = "wVEpDxPf1XRmvYmrZHgvULaGM", secret = "7q9Z1DuogwyZtdeYkkgxzkmIW8qTcfJRH0x9IXUFNw5gmJtNEs")
    val accessToken = AccessToken(key = "3317308574-mgg8AWsjQoLhReURzpIZ3htV9t3dMfsNNEBcvBF", secret = "GOyWnhDoq8imihe47y2E6BsTrLvOxQjz3P2HKlAZkHdrA")
    val restClient = TwitterRestClient(consumerToken, accessToken)
    implicit val ec = ExecutionContext.global



    //building block methods for the images
    def nextColor(color: Color): Random[Color] = {
      val spin = Random.normal(15.0, 10.0)
      spin map { s => color.spin(Angle(s)) }
    }

    val r = scala.util.Random

    def coloredCircle(color: Color, size: Double): Image =
      Image.circle(size).strokeWidth(1.0).strokeColor(color.spin(Angle(180))).fillColor(color)

    /*
    //define a variety of methods to create different image styles
    def sundayImage(count: Int, color: Color, size: Double): Random[Image] =
      count match {
        case 0 => Random.always(Image.empty)
        case n =>
          val loop = coloredCircle(color, size)
          val loops = nextColor(color) flatMap { c => sundayImage(n - 1, c, size + r.nextInt(7)) }
          loops map { l => (loop beside loop beside loop) above (loop beside loop beside loop) above (loop beside loop beside loop) on l }
      }

    def mondayImage(count: Int, color: Color, size: Double): Random[Image] =
      count match {
        case 0 => Random.always(Image.empty)
        case n =>
          val loop = coloredCircle(color, size)
          val loops = nextColor(color) flatMap { c => mondayImage(n - 1, c, size + r.nextInt(7)) }
          loops map { l => loop above (loop beside loop beside loop) above loop on l }
      }

    def tuesdayImage(count: Int, color: Color, size: Double): Random[Image] =
      count match {
        case 0 => Random.always(Image.empty)
        case n =>
          val loop = coloredCircle(color, size)
          val loops = nextColor(color) flatMap { c => tuesdayImage(n - 1, c, size + r.nextInt(7)) }
          loops map { l => (loop beside loop) below loop on l }
      }

    def wednesdayImage(count: Int, color: Color, size: Double): Random[Image] =
      count match {
        case 0 => Random.always(Image.empty)
        case n =>
          val loop = coloredCircle(color, size)
          val loops = nextColor(color) flatMap { c => wednesdayImage(n - 1, c, size + r.nextInt(7)) }
          loops map { l => (loop beside loop beside loop beside loop beside loop) on l }
      }

    def thursdayImage(count: Int, color: Color, size: Double): Random[Image] =
      count match {
        case 0 => Random.always(Image.empty)
        case n =>
          val loop = coloredCircle(color, size)
          val loops = nextColor(color) flatMap { c => thursdayImage(n - 1, c, size + r.nextInt(7)) }
          loops map { l => loop above
              (loop beside loop beside loop) above
              (loop beside loop beside loop beside loop beside loop) above
              (loop beside loop beside loop) above
              loop above
              (loop beside loop beside loop) above
              (loop beside loop beside loop beside loop beside loop) above
              (loop beside loop beside loop) above
              loop on l }
      }

    def fridayImage(count: Int, color: Color, size: Double): Random[Image] =
      count match {
        case 0 => Random.always(Image.empty)
        case n =>
          val loop = coloredCircle(color, size)
          val loops = nextColor(color) flatMap { c => fridayImage(n - 1, c, size + r.nextInt(7)) }
          loops map { l => loop above
            (loop beside loop beside loop) above
            (loop beside loop beside loop beside loop beside loop) above
            (loop beside loop beside loop) above
            loop on l }
      }

    def saturdayImage(count: Int, color: Color, size: Double): Random[Image] =
      count match {
        case 0 => Random.always(Image.empty)
        case n =>
          val loop = coloredCircle(color, size)
          val loops = nextColor(color) flatMap { c => saturdayImage(n - 1, c, size + r.nextInt(7)) }
          loops map { l => loop above
            (loop beside loop beside loop) above
            (loop beside loop beside loop) above
            (loop beside loop beside loop beside loop beside loop) above
            (loop beside loop beside loop) above
            (loop beside loop beside loop) above
            loop on l }
      }
     */

    //method to create an image based on day of week
    def dayImage(count: Int, color: Color, size: Double, dayOfWeek: DayOfWeek): Random[Image] =
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

    //get the current day of the week
    val now = Calendar.getInstance()
    val currentDay = now.get(Calendar.DAY_OF_WEEK)
    val weekDays = Seq("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    val today = weekDays(currentDay-1)

    //create name for new file and create a file path for it
    //case object fileName {
    //  val id = randomUUID().toString
    //  val newName = id.concat(".png")
    //}
    //val filePath = "/Users/michelleaustin/repos/twitterbot/" + fileName.newName

    //create an image for today and write to file
    //pickDay(today).run.write[Png](filePath)
    //dayImage(r.nextInt(50), nextColor(Color.crimson).run, r.nextInt(10), dayToString(today)).run.write[Png](filePath)

    //schedule the tweet
    val t = new java.util.Timer()
    val task = new java.util.TimerTask {
      def run() = {
        case object fileName {
          val id = randomUUID().toString
          val newName = id.concat(".png")
        }
        val filePath = "/Users/michelleaustin/repos/twitterbot/" + fileName.newName
        dayImage(r.nextInt(50), nextColor(Color.crimson).run, r.nextInt(10), dayToString(today)).run.write[Png](filePath)
        for {
          upload <- restClient.uploadMediaFromPath(filePath)
          tweet <- restClient.createTweet("Happy " + today + "!", media_ids = Seq(upload.media_id))
        } yield tweet
      }
    }
    //t.schedule(task, 1000L, 1000L)
    now.set(Calendar.HOUR_OF_DAY, 14)
    now.set(Calendar.MINUTE, 38)
    now.set(Calendar.SECOND, 0)

    //val oncePerDay = 1000*60*60*24
    val oncePerDay = 1000*60

    t.schedule(task, 1000L, oncePerDay)



    //sleep thread
    //while (true) {
    //  Thread.sleep(1000 * 60)
    //}

  }


}
