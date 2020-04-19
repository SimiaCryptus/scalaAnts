package com.simiacryptus.ant.js

import org.scalajs.dom

class Timer(run: () => Unit, speed: Int) {
  var id: Option[Int] = None

  def start() = {
    if (id.isDefined) throw new IllegalStateException()
    id = Option(dom.window.setInterval(run, speed))
    this
  }

  def stop() = {
    dom.window.clearInterval(id.get)
    id = None
    this
  }
}
