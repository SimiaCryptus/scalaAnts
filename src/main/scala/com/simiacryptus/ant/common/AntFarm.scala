package com.simiacryptus.ant.common

import scala.collection.mutable.ArrayBuffer

class AntFarm(val width: Int = 800, val height: Int = 800) {
  val ants = new ArrayBuffer[Ant]
  val data: Array[Byte] = new Array[Byte](width * height)

  def get(p: Point): Byte = {
    val point = p.mod(width, height)
    val index = toIndex(point.x, point.y)
    val value = data(index)
    value
  }

  private def toIndex(x: Int, y: Int) = (x * height) + y

  def set(p: Point, value: Byte): Point = {
    val point = p.mod(width, height)
    val index = toIndex(point.x, point.y)
    data(index) = value
    point
  }

  def step(): Unit = {
    if (ants.isEmpty) println("No Ants")
    require(!ants.isEmpty)
    for (ant <- ants) {
      ant.step()
    }
  }

  def add(ant: Ant): Unit = {
    ant.farm = this
    ants += ant
  }

  def clear(): Unit = {
    java.util.Arrays.fill(data, 0.toByte)
  }
}
