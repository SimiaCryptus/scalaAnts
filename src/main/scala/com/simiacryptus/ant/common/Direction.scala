package com.simiacryptus.ant.common

object Direction extends Enumeration {

  def getDirection(c: Char): Direction = {
    if ('R' == c) Counterclockwise
    else if ('L' == c) Clockwise
    else if ('S' == c) Straight
    else throw new RuntimeException("Unknown code character: " + c)
  }

  object Straight extends Direction {
    override def transform(from: Orientation): Orientation = from
  }

  object Clockwise extends Direction {
    override def transform(from: Orientation): Orientation = from.clockwise
  }

  object Counterclockwise extends Direction {
    override def transform(from: Orientation): Orientation = from.counterClockwise
  }

}

sealed abstract class Direction {
  val name = getClass.getSimpleName.stripSuffix("$")

  def transform(from: Orientation): Orientation

  override def toString = name
}