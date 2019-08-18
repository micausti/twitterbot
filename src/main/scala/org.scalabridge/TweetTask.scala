package org.scalabridge

import java.util.Calendar

import com.typesafe.scalalogging.Logger
import doodle.image._
import doodle.image.syntax._
import doodle.core._
import doodle.java2d._
import doodle.effect.Writer._

import scala.concurrent.ExecutionContext
import com.typesafe.config.ConfigFactory
import com.danielasfregola.twitter4s.{TwitterRestClient, entities}
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}



class TweetTask() {

  val logger = Logger("TweetTask logger")
  logger.info("defining day, file path, and image for today's tweet")

  val task = new java.util.TimerTask {

    def run(): Unit = {
      implicit val ec = ExecutionContext.global

      val consumerToken = ConsumerToken(key = sys.env("TWITTER_CONSUMER_TOKEN_KEY"), secret = sys.env("TWITTER_CONSUMER_TOKEN_SECRET"))
      val accessToken = AccessToken(key = sys.env("TWITTER_ACCESS_TOKEN_KEY"), secret = sys.env("TWITTER_ACCESS_TOKEN_SECRET"))
      val restClient = TwitterRestClient(consumerToken, accessToken)
      logger.info("consumerToken"  + consumerToken)
      logger.info("accessToken" + accessToken)

      val today = new getDay().getDayOfWeek(new getDay().numOfDay())
      val makeToday = new MakeTweet
      val filePathForToday = makeToday.filePath()
      val statusForToday = makeToday.status(today)
      val makeImageForToday = new ImageBuilder().dayImage(50, Color.crimson, 10, today: String)
      val imageForToday = makeImageForToday.run.write[Png](filePathForToday)
      logger.info("creating today's tweet with day " + today)
      logger.info("file Path for tweet " + filePathForToday)
      logger.info("status for tweet " + statusForToday)
      for {
        upload <- restClient.uploadMediaFromPath(filePathForToday)
        tweet <- restClient.createTweet(statusForToday, media_ids = Seq(upload.media_id))
      } yield tweet
    }
  }
}


