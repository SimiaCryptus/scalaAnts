package com.simiacryptus.ant

import com.simiacryptus.ant.zoo.ZooGenerator


object CreateAntZoo {
  def main(args: Array[String]): Unit = {
    ZooGenerator(
      reportName = "basic",
      maxStates = 1,
      maxColors = 2,
      allowStraight = false,
      allowDuplicates = true
    ).write()
    generate("_simple", 1, 8)
    generate("_complex", 8, 9)
  }

  def generate(suffix: String, minBits: Int, maxBits: Int): Unit = {
    ZooGenerator(
      reportName = "turnites" + suffix,
      minStates = 2,
      maxStates = 8,
      maxColors = 2,
      maxBits = maxBits,
      minBits = minBits
    ).write()
    ZooGenerator(
      reportName = "colorTurnites" + suffix,
      minStates = 2,
      maxStates = 8,
      minColors = 3,
      maxColors = 8,
      maxBits = maxBits,
      minBits = minBits
    ).write()
    ZooGenerator(
      reportName = "colors" + suffix,
      maxStates = 1,
      minColors = 3,
      maxColors = 8,
      maxBits = maxBits,
      minBits = minBits
    ).write()
  }
}

