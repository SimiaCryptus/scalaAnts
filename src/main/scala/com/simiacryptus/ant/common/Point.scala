package com.simiacryptus.ant.common

case class Point(x: Int, y: Int) {
  def add(d: Orientation) = Point(x + d.dx, y + d.dy)

  def mod(width: Int, height: Int): Point = {
    var nx = x % width
    var ny = y % height
    while ( {
      nx < 0
    }) nx += width
    while ( {
      ny < 0
    }) ny += height
    new Point(nx, ny)
  }
}
