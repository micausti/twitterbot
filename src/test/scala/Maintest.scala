import java.util.{Calendar, TimerTask}

import org.scalabridge.{MakeTweet, ScheduleTweet, getDay}
import org.scalatest.FlatSpec



class SetSpec extends FlatSpec {
  "getDay.getDayOfWeek method" should "get a calendar instance and determine the day of the week" in {
    val dayOfWeekTest = new getDay()
    assert(dayOfWeekTest.getDayOfWeek(1) == "Sunday")
    assert(dayOfWeekTest.getDayOfWeek(2) == "Monday")
    assert(dayOfWeekTest.getDayOfWeek(3) == "Tuesday")
    assert(dayOfWeekTest.getDayOfWeek(4) == "Wednesday")
    assert(dayOfWeekTest.getDayOfWeek(5) == "Thursday")
    assert(dayOfWeekTest.getDayOfWeek(6) == "Friday")
    assert(dayOfWeekTest.getDayOfWeek(7) == "Saturday")
    }




  "MakeTweet.status method" should "create a status for the day of the week with the format 'Happy DAY! '" in {
    val statusTest = new MakeTweet
    assert(statusTest.status("Sunday") == "Happy Sunday!")
    assert(statusTest.status("Monday") == "Happy Monday!")
    assert(statusTest.status("Tuesday") == "Happy Tuesday!")
    assert(statusTest.status("Wednesday") == "Happy Wednesday!")
    assert(statusTest.status("Thursday") == "Happy Thursday!")
    assert(statusTest.status("Friday") == "Happy Friday!")
    assert(statusTest.status("Saturday") == "Happy Saturday!")
  }

 "schedule tweet for today" should "schedule the task daily run to be performed at the now interval, and repeated at the tomorrow interval" in {
   val tweetTest = new ScheduleTweet
   val now = Calendar.getInstance().getTime
   val tomorrow = 10000
   def dailyRun: TimerTask =  new java.util.TimerTask {
     def run(): Unit = {
       println(Calendar.MINUTE)
     }
   }
   tweetTest.scheduleTask(dailyRun, now, tomorrow)

     //if the task is initiated on a Monday, when the time ticks over to the next Tuesday, the task should be re-run

   //assert(tweetTest.scheduleTask(dailyRun, now, tomorrow) == "TestRound")

 }
  }





