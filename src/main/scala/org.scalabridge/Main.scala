package org.scalabridge


import java.util.{Calendar, TimeZone, Timer, TimerTask, Date}
import java.util.concurrent.TimeUnit

import com.typesafe.scalalogging._


object Main {

  def main(args: Array[String]): Unit = {

    //find out the day of the week
    //make the image for today
    //Create the tweet for today with the correct status and file path
    //Schedule the task

    val logger = Logger("main logger")

    logger.info("Starting Process")
    logger.info("Create Tweet for the Day")
    val scheduleTweetForToday = new ScheduleTweet
    //def dailyRun = scheduleTweetForToday.dailyRun
    val now = Calendar.getInstance().getTime
    //val tomorrow =  TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
    val tomorrow = 10000
    scheduleTweetForToday.scheduleTask(new ScheduleTweet(), now, tomorrow)
    logger.info("Tweet is scheduled to run at " + now + " with a repeat interval of " + tomorrow + " ms")
    logger.info("Process is complete, awaiting next run")

 }

}
