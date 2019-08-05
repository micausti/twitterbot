package org.scalabridge

import com.typesafe.scalalogging.Logger
import doodle.core.{Angle, Color}
import doodle.image.Image
import doodle.random.Random

class ImageBuilder() {

  val logger = Logger("ImageBuilder logger")


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
        val looper = loop beside loop beside loop
        val loooooper = loop beside loop beside loop beside loop beside loop
        val loops = nextColor(color) flatMap { c => dayImage(n - 1, c, size + scala.util.Random.nextInt(7), today) }
        today match {
          case "Sunday" => loops map { l => looper above looper above looper on l }
          case "Monday" => loops map { l => loop above looper above loop on l }
          case "Tuesday" => loops map { l => (loop beside loop) below loop on l }
          case "Wednesday" => loops map { l => loooooper on l }
          case "Thursday" => loops map { l => loop above looper above loooooper above looper above loop above looper above loooooper above looper above loop on l }
          case "Friday" => loops map { l => loop above looper above loooooper above looper above loop on l }
          case "Saturday" => loops map { l => loop above looper above looper above loooooper above looper above looper above loop on l }
        }
    }
  }
}
