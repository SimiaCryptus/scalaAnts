package com.simiacryptus.ant.js

import com.simiacryptus.ant.common.Ant
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.html.Canvas
import org.scalajs.dom.raw.{HashChangeEvent, Location, MouseEvent, UIEvent}

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("AntLib")
object AntLib {

  @JSExport
  def navigate() = {
    val code = dom.window.location.hash.stripPrefix("#")
    if(!code.isEmpty) {
      antPopup(code)
    }
    dom.window.onhashchange = (e:HashChangeEvent) => {
      val code = dom.window.location.hash.stripPrefix("#")
      if(!code.isEmpty) {
        antPopup(code)
      } else if(dialog != null) {
        dialog.close()
      }
    }
  }

  private var dialog: AntDialog = null
  @JSExport
  def antPopup(code: String) = {
    if(dialog == null || dialog.code != code) {
      if(dialog != null) {
        dialog.close(false)
      }
      val width = (dom.window.innerWidth).asInstanceOf[Int]
      val height = (dom.window.innerHeight).asInstanceOf[Int]
      val size = Math.min(width, height)
      dialog = new AntLib.AntDialog(size, size, (size * 0.05).toInt, code).show()
      //dialog = new AntLib.AntDialog(width, height, (Math.max(width, height) * 0.05).toInt, code).show()
      dom.window.location.hash = code
    }
  }

  class AntDialog(val width: Int, val height: Int, val border: Int, val code: String) {

    lazy val canvas = {
      val element = dom.document.createElement(s"canvas").asInstanceOf[Canvas]
      element.width = width-2*border
      element.height = height-2*border
      popup.appendChild(element)
      element
    }

    lazy val popup = {
      val element = dom.document.createElement(s"div").asInstanceOf[html.Div]
      element.style =
        s"""
           |float: left;
           |width: ${width-2*border}px;
           |height: ${height-2*border}px;
           |top: ${border}px;
           |left: ${border}px;
           |position: fixed;
           |background-color: white;
           |""".stripMargin.trim
      element.onclick = (event: MouseEvent) => {
        close()
      }
      dom.document.body.appendChild(element)
      element
    }

    lazy val farm: JsAntFarm = {
      val jsAntFarm = new JsAntFarm(canvas)
      jsAntFarm.add(new Ant(width / 2, height / 2, code))
      jsAntFarm
    }
    var timer: Timer = null

    def show() = {
      farm.stepsBetweenClear = 0
      this.timer = farm.start(50)
      this
    }

    def close(navigate: Boolean = true): Unit = {
      popup.parentNode.removeChild(popup)
      Option(this.timer).foreach(_.stop())
      this.timer = null
      dialog = null
      if(navigate) dom.window.location.hash = ""
    }
  }

}
