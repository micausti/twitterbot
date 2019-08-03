package org.scalabridge

import com.danielasfregola.twitter4s.TwitterRestClient
import com.typesafe.scalalogging.Logger
import doodle.image._
import doodle.image.syntax._
import doodle.core._
import doodle.java2d._
import doodle.effect.Writer._
import scala.concurrent.ExecutionContext

class TweetTask {

  val logger = Logger("TweetTask logger")
  logger.info("defining day, file path, and image for today's tweet")

  implicit val ec = ExecutionContext.global
  val restClient = TwitterRestClient()
  val today = new getDay().getDayOfWeek(new getDay().numOfDay())
  val makeToday = new MakeTweet
  val filePathForToday = makeToday.filePath()
  val statusForToday = makeToday.status(today)
  val makeImageForToday = new ImageBuilder().dayImage(50, Color.crimson, 10, today:String)
  val imageForToday = makeImageForToday.run.write[Png](filePathForToday)


  def task = new java.util.TimerTask {
    logger.info("creating today's tweet with day " + today)
    logger.info("file Path for tweet" + filePathForToday)
    logger.info("status for tweet" + statusForToday)
    def run(): Unit = {
      for {
        upload <- restClient.uploadMediaFromPath(filePathForToday)
        tweet <- restClient.createTweet(statusForToday, media_ids = Seq(upload.media_id))
      } yield tweet
    }
  }
}
