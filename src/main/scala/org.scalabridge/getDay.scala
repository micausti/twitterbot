package org.scalabridge

import java.util.Calendar

import com.typesafe.scalalogging.Logger

class getDay() {

  val logger = Logger("getDay logger")

  def numOfDay() = {
    Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
  }

  def getDayOfWeek(dayOfWeek: Int): String =  {
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

  def today(): String = {
    new getDay().getDayOfWeek(new getDay().numOfDay())
  }

}