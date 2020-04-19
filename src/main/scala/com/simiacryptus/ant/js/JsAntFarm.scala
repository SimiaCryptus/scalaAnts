package com.simiacryptus.ant.js

import com.simiacryptus.ant.common.{AntFarm, Point}
import org.scalajs.dom
import org.scalajs.dom.html

class JsAntFarm(val canvas: html.Canvas) extends AntFarm(canvas.width, canvas.height) {
  val colors = Array("white", "black", "red", "green", "blue", "violet", "orange", "yellow")
  val context2d: dom.CanvasRenderingContext2D = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
  val maxSpeed = 1000
  var stepsBetweenClear = 1000
  var stepSize = 1
  var stepsUntilClear = 10000

  override def set(p: Point, value: Byte): Point = {
    val p2 = super.set(p, value)
    context2d.fillStyle = colors(value)
    context2d.fillRect(p2.x, p2.y, 1, 1)
    p2
  }

  def start(speed: Int) = {
    if (ants.isEmpty) println("No Ants")
    new Timer(() => {
          if (0 < stepsBetweenClear && 0 >= {
            stepsUntilClear -= 1;
            stepsUntilClear + 1
          }) {
            stepsUntilClear = stepsBetweenClear
            clear()
          } else {
            val start = System.currentTimeMillis
            (0 until stepSize).foreach(_ => step())
            val time = System.currentTimeMillis - start
            stepSize = Math.min(maxSpeed, Math.max(1, (stepSize * (speed.toDouble / time)).ceil.toInt))
            //println(s"StepSize=$stepSize")
          }
        }, speed).start()
  }

  override def clear(): Unit = {
    println("Clearing AntFarm")
    context2d.fillStyle = colors(0)
    context2d.fillRect(0, 0, width, height)
    super.clear()
  }
}


