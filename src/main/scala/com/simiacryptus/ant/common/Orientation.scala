package com.simiacryptus.ant.common

object Orientation {

  object Up extends Orientation(0, 1) {
    override def counterClockwise: Orientation = Left

    override def clockwise: Orientation = Right
  }

  object Down extends Orientation(0, -1) {
    override def counterClockwise: Orientation = Right

    override def clockwise: Orientation = Left
  }

  object Left extends Orientation(1, 0) {
    override def counterClockwise: Orientation = Down

    override def clockwise: Orientation = Up
  }

  object Right extends Orientation(-1, 0) {
    override def counterClockwise: Orientation = Up

    override def clockwise: Orientation = Down
  }

}

sealed abstract class Orientation
(
  val dx: Int,
  val dy: Int
) {
  val name = getClass.getSimpleName.stripSuffix("$")

  def counterClockwise: Orientation

  def clockwise: Orientation

  override def toString = name
}