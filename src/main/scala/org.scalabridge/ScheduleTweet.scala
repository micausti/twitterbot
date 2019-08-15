package org.scalabridge

import java.util.{Calendar, Date, TimerTask}
import java.util.concurrent.TimeUnit

import com.typesafe.scalalogging.Logger


class ScheduleTweet {

  val logger = Logger("ScheduleTweet logger")

  def dailyRun =  {
    val dailyRun = new TweetTask
    dailyRun.task
  }

  def scheduleTask(task: TimerTask, time: Date, repeat: Long): Unit = {
    logger.info("scheduling the run to start")
    val t = new java.util.Timer()
    t.schedule(task, time, repeat)
  }

}