package com.simiacryptus.ant.zoo

import java.awt.image.BufferedImage
import java.io.{ByteArrayOutputStream, File}

import com.simiacryptus.ant.common.{AntFarm, Point}
import javax.imageio.ImageIO

class AntFarmImage(val w: Int = 800, val h: Int = 800) extends AntFarm(w, h) with Runnable {
  val colors = Array(Array(0, 0, 0), Array(255, 255, 255), Array(0, 255, 255), Array(255, 0, 255), Array(255, 255, 0), Array(255, 0, 0), Array(0, 255, 0), Array(0, 0, 255))
  final val histogram = {
    val array = new Array[Int](colors.length)
    array(0) = width * height
    array
  }
  final var image: BufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

  def fillRatio: Double = 1. - (histogram(0).toDouble / (width * height))

  override def set(p: Point, value: Byte): Point = {
    val oldValue = super.get(p)
    histogram(oldValue) -= 1
    histogram(value) += 1
    val p2 = super.set(p, value)
    val color = colors(value)
    image.getRaster.setPixel(p2.x, p2.y, color)
    p2
  }

  def run(generations: Int): Unit = {
    (0 until generations).foreach(_ => step())
  }

  override def run(): Unit = {
    while ( {
      this.continueLoop
    }) step()
  }

  private var continueLoop = true

  def stop(): Unit = {
    this.continueLoop = false
  }

  def png(file: File): Unit = {
    ImageIO.write(image, "png", file)
  }

  def png(): String = {
    val stream = new ByteArrayOutputStream
    ImageIO.write(image, "png", stream)
    "data:image/png;base64," + javax.xml.bind.DatatypeConverter.printBase64Binary(stream.toByteArray)
  }
}

object AntFarmImage {

  trait AwtAntFarmEvents {
    def onChange(): Unit
  }

}