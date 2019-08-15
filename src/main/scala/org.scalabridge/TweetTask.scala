package org.scalabridge

import java.util.Calendar

import com.danielasfregola.twitter4s.TwitterRestClient
import com.typesafe.scalalogging.Logger
import doodle.image._
import doodle.image.syntax._
import doodle.core._
import doodle.java2d._
import doodle.effect.Writer._

import scala.concurrent.ExecutionContext
import com.typesafe.config.ConfigFactory

class TweetTask() {

  val logger = Logger("TweetTask logger")
  logger.info("defining day, file path, and image for today's tweet")

  val task = new java.util.TimerTask {

    def run(): Unit = {
      implicit val ec = ExecutionContext.global
      val restClient = TwitterRestClient()


      /*
      val twconsumerkey = ConfigFactory.load().getString("twitter.consumer.key")
      val twconsumersecret = ConfigFactory.load().getString("twitter.consumer.secret")
      val twaccesskey = ConfigFactory.load().getString("twitter.access.key")
      val twaccesssecret = ConfigFactory.load().getString("twitter.access.secret")
      println(s"My twitter consumer key is $twconsumerkey")
      println(s"My twitter consumer secret is $twconsumersecret")
      println(s"My twitter access key is $twaccesskey")
      println(s"My twitter access secret is $twaccesssecret")
      */
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


