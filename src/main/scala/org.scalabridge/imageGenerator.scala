package org.scalabridge


import doodle.image._
import doodle.image.syntax._
import doodle.core._
import doodle.java2d._
import doodle.effect.Writer._

import doodle.java2d.effect.Frame
import doodle.random.Random

/*
object New {
  val image = Image.circle(100).strokeWidth(10.0).fillColor(Color.crimson)

  image.write[Png]("circle.png")
}
*/

/*
object Make {

  def nextColor(color: Color): Random[Color] = {
    val spin = Random.normal(10.0, 100.0)
    spin map { s => color.spin(s.degrees) }
  }

  val frame = Frame.fitToImage().background(Color.black)

  def coloredCircle(color: Color, size: Double): Image =
    Image.circle(size).strokeWidth(1.0).strokeColor(color.spin(180.degrees)).fillColor(color)

  def randomGradientLoop(count: Int, color: Color, size: Double): Random[Image] =
    count match {
      case 0 => Random.always(Image.empty)
      case n =>
        val loop = coloredCircle(color, size)
        val loops = nextColor(color) flatMap { c => randomGradientLoop(n - 1, c, size + 3) }
        loops map { l => (loop beside loop beside loop) above (loop beside loop beside loop) above (loop beside loop beside loop) on l }
    }

}
*/




/*
//create a counter to increment id by 1 for each new image
case class Counter{
  val id = 0
  def inc = new Counter(id + 1)
}
//creates a new file name
case class FileName(id:Counter, date:String) {
  def name = date + "_" + id + "png"
}

//create the image and write to file, using the file name created from sequential id and date
val newFile = new FileName(new Counter, date = "date")

*/
