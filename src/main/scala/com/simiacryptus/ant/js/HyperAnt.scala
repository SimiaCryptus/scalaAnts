package com.simiacryptus.ant.js

import java.util.Random

import com.simiacryptus.ant.common.Ant
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.html.Canvas
import org.scalajs.dom.raw.MouseEvent

import scala.collection.mutable.ArrayBuffer
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

object HyperAnt {
  def createAntFarm(canvas: html.Canvas) = new JsAntFarm(canvas)
}

@JSExportTopLevel("HyperAnt")
class HyperAnt {
  val random = new Random
  var timer: Timer = null

  @JSExport
  def scan(canvas: html.Canvas): Unit = {
    loadAnts(canvas, getAntElements(canvas.width, canvas.height))
  }

  def getAntElements(width: Int, height: Int) = {
    val list = new ArrayBuffer[Ant]
    val element = dom.document
    val childNodes = element.childNodes
    var isFirst = true
    var i = 0
    while ( {
      i < childNodes.length
    }) {
      val item = childNodes(i)
      if ("ANT" == item.nodeName) {
        var j = 0
        while ( {
          j < item.childNodes.length
        }) {
          val item1 = item.childNodes(j)
          if ("#text" == item1.nodeName) {
            var x = 0
            var y = 0
            if (isFirst) {
              x = (0.5 * width).toInt
              y = (0.5 * height).toInt
              isFirst = false
            }
            else {
              x = (random.nextDouble * width).toInt
              y = (random.nextDouble * height).toInt
            }
            list += (new Ant(x, y, item1.nodeValue))
            item1.nodeValue = ""
          }

          j += 1
        }
      }

      i += 1
    }
    list.toList
  }

  @JSExport
  def codes(canvas: html.Canvas, codes: String*): Unit = {
    loadAnts(canvas, getAntElements(canvas.width, canvas.height, codes))
  }

  @JSExport
  val stepsBetweenClear = 10000

  @JSExport
  val speed = 50

  private def loadAnts[T <: Ant](canvas: Canvas, ants: Seq[T]) = {
    require(!ants.isEmpty)
    if (ants.isEmpty) println("No Ants")
    val farm = new JsAntFarm(canvas)
    for (ant <- ants) {
      farm.add(ant)
    }
    farm.stepsBetweenClear = stepsBetweenClear
    timer = farm.start(speed)
    canvas.onclick = (e: MouseEvent) => {
      if (null != timer) {
        timer.stop()
        timer = null
      } else {
        timer = farm.start(speed)
      }
    }
  }

  def getAntElements(width: Int, height: Int, codes: Seq[String]) = {
    var isFirst = true
    for (code <- codes) yield {
      var x = 0
      var y = 0
      if (isFirst) {
        x = (0.5 * width).toInt
        y = (0.5 * height).toInt
        isFirst = false
      }
      else {
        x = (random.nextDouble * width).toInt
        y = (random.nextDouble * height).toInt
      }
      new Ant(x, y, code)
    }
  }
}
