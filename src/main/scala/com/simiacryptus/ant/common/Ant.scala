package com.simiacryptus.ant.common

object Ant {

  case class Rule(direction: Direction, newColor: Int, newRule: Int)

}

class Ant(val x: Int, val y: Int, val code: String) {
  val lines: Array[String] = code.toUpperCase.split("\n|/")
  val rules = for ((line, ruleIndex) <- lines.zipWithIndex) yield {
    val ruleChars: Array[Char] = line.toCharArray
    for ((c, charIndex) <- ruleChars.zipWithIndex) yield {
      val direction: Direction = Direction.getDirection(c)
      val newRule: Int = (ruleIndex + 1) % lines.length
      val newColor: Int = (charIndex + 1) % ruleChars.length
      Ant.Rule(direction, newColor, newRule)
    }
  }
  var orientation: Orientation = Orientation.Down
  var farm: AntFarm = null
  var point: Point = Point(x, y)
  var state: Int = 0

  def step(): Unit = {
    val oldColor = farm.get(point)
    val ruleSet: Array[Ant.Rule] = rules(state)
    val rule: Ant.Rule = ruleSet(oldColor % ruleSet.size)
    state = rule.newRule
    orientation = rule.direction.transform(orientation)
    farm.set(point, rule.newColor.toByte)
    val newPoint = point.add(orientation)
    point = newPoint
  }

  override def toString: String = code
}
