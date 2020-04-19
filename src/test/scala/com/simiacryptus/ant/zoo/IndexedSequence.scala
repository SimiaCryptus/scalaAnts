package com.simiacryptus.ant.zoo

class IndexedSequence(var value: Int, var max: Int) {
  def hasNext(base: Int): Boolean = base <= max

  def getNext(base: Int): Int = {
    val next = value % base
    value = value / base
    max = max / base
    next
  }
}
