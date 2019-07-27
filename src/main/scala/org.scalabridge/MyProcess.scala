package org.scalabridge

import doodle.core.{Angle, Color}
import doodle.image.Image
import doodle.random.Random

import java.util.{Calendar, TimeZone, Timer, TimerTask}
import java.util.UUID.randomUUID
import java.util.concurrent.TimeUnit

import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}


sealed trait Tweet {
  val consumerToken = ConsumerToken(key = "wVEpDxPf1XRmvYmrZHgvULaGM", secret = "7q9Z1DuogwyZtdeYkkgxzkmIW8qTcfJRH0x9IXUFNw5gmJtNEs")
  val accessToken = AccessToken(key = "3317308574-mgg8AWsjQoLhReURzpIZ3htV9t3dMfsNNEBcvBF", secret = "GOyWnhDoq8imihe47y2E6BsTrLvOxQjz3P2HKlAZkHdrA")
  val restClient = TwitterRestClient(consumerToken, accessToken)

  //def image(count: Int, color: Color, size: Double, today: String): Random[Image] ={
    //TODO fix this - how to create the image here
    //ImageBuilder.dayImage(count, color, size, today).run.write[Png](filePath)
  //}

  def filePath(): String = {
    "/tmp/" + randomUUID().toString + ".png"
  }
  def status(today: String): String = {
    "Happy " + today + "!"
  }
}

sealed trait ImageFeatures {
  def color: Color
}

final case class NextColor(color: Color, spin: Random[Double] = Random.normal(15.0, 10.0) ) extends ImageFeatures {
  def apply = spin map { s => color.spin(Angle(s)) }
}
final case class ColoredCircle(color: Color, size: Double) extends ImageFeatures {
  def apply = Image.circle(size).strokeWidth(1.0).strokeColor(color.spin(Angle(180))).fillColor(color)
}



//TODO need to refactor this section to get rid of the duplicate methods and to refactor the dayimage method
class ImageBuilder(count: Int, color: Color, size: Double, today: String) {


  def nextColor(color: Color): Random[Color] = {
    //building blocks for the imageS, selects the next color to be used
    val spin = Random.normal(15.0, 10.0)
    spin map { s => color.spin(Angle(s)) }
  }

  def coloredCircle(color: Color, size: Double): Image = {
    //building blocks for images, generates a colored circle
    Image.circle(size).strokeWidth(1.0).strokeColor(color.spin(Angle(180))).fillColor(color)
  }

  def dayImage(count: Int, color: Color, size: Double, today:String): Random[Image] = {
    //create an image based on day of week
    count match {
      case 0 => Random.always(Image.empty)
      case n =>
        val loop = coloredCircle(color, size)
        val loops = nextColor(color) flatMap { c => dayImage(n - 1, c, size + scala.util.Random.nextInt(7), today) }
        today match {
          case "Sunday" => loops map { l => (loop beside loop beside loop) above (loop beside loop beside loop) above (loop beside loop beside loop) on l }
          case "Monday" => loops map { l => loop above (loop beside loop beside loop) above loop on l }
          case "Tuesday" => loops map { l => (loop beside loop) below loop on l }
          case "Wednesday" => loops map { l => (loop beside loop beside loop beside loop beside loop) on l }
          case "Thursday" => loops map { l => loop above (loop beside loop beside loop) above (loop beside loop beside loop beside loop beside loop) above (loop beside loop beside loop) above loop above (loop beside loop beside loop) above (loop beside loop beside loop beside loop beside loop) above (loop beside loop beside loop) above loop on l }
          case "Friday" => loops map { l => loop above (loop beside loop beside loop) above (loop beside loop beside loop beside loop beside loop) above (loop beside loop beside loop) above loop on l }
          case "Saturday" => loops map { l => loop above (loop beside loop beside loop) above (loop beside loop beside loop) above (loop beside loop beside loop beside loop beside loop) above (loop beside loop beside loop) above (loop beside loop beside loop) above loop on l }
        }
    }
  }
}

class today {
  def apply(now:Int): String =  {
    this match {
      case 1 => "Sunday"
      case 2 => "Monday"
      case 3 => "Tuesday"
      case 4 => "Wednesday"
      case 5 => "Thursday"
      case 6 => "Friday"
      case 7 => "Saturday"
    }
  }
}

//TODO move timer task and schedule task  sections to the main method

 /*
 object TimerTask {
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
    }
*/
  object scheduleTweet {
   def apply(task: TimerTask, now: Calendar): Unit = {
     //schedule the tweet and repeat
     val t = new java.util.Timer()
     t.schedule(task, now.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))
   }
 }







