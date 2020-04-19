package com.simiacryptus.ant.zoo

import java.io.{File, PrintStream}

import com.simiacryptus.ant.common.Ant

import scala.collection.mutable

case class ZooGenerator
(
  width: Int = 128,
  height: Int = 128,
  stepGenerations: Int = 1000,
  maxGenerations: Int = 50000,
  allowStraight: Boolean = true,
  inlineSrc: Boolean = true,
  reportName: String = "zoo",
  baseDir: File = new File("src/main/resources/zoo/"),
  javascriptPath: String = "scalaants-opt.js",
  maxColors: Int = 5,
  minStates: Int = 1,
  maxStates: Int = 5,
  minColors: Int = 2,
  minBits: Int = 0,
  maxBits: Int = 10,
  allowDuplicates : Boolean = false,
  pageSize: Int = 1024,
  minFill: Double = 0.01,
  targetFill: Double = 0.5
) {
  private var imageIdx: Int = 0
  private var zooDir: File = null
  private val dedupSet = new mutable.HashSet[AntSignature]

  def write(ants: Seq[Ant] = getAllAnts()): Unit = {
    baseDir.mkdirs()
    if (!inlineSrc) {
      this.zooDir = new File(baseDir, reportName)
      zooDir.mkdirs
    }
    for((ants, pageNumber) <- ants.toParArray.map(render(_)).filter(!_.isEmpty).toStream.grouped(pageSize).zipWithIndex) {
      val htmlOut = new PrintStream(new File(baseDir, s"$reportName.$pageNumber.html"))
      try {
        htmlOut.print(
          s"""
            |<html>
            |<head>
            |    <title>Langton Ants in HTML5</title>
            |    <script type="text/javascript" language="javascript" src="$javascriptPath"></script>
            |</head>
            |<body onload="AntLib.navigate()">
            |  ${ants.reduce(_+"\n  "+_)}
            |</body>
            |</html>""".stripMargin)
      } finally {
        htmlOut.close()
      }
    }
  }

  def getAllAnts() = {
    val base = if(allowStraight) 3 else 2
    (for (
      states <- minStates to maxStates;
      colors <- minColors to maxColors
    ) yield {
      if ((states * colors) < maxBits && (states * colors) >= minBits) {
        val m = Math.pow(base, colors * states).toInt
        for (i <- 0 until m) yield {
          val sb = new StringBuilder
          val source = new IndexedSequence(i, m)
          var chars = 0
          while (source.hasNext(base)) {
            chars += 1
            val n = source.getNext(base)
            if (0 == n) sb.append("R")
            else if (1 == n) sb.append("L")
            else sb.append("S")
            if (0 == (chars % colors) && source.hasNext(base)) sb.append("/")
          }
          new Ant(width / 2, height / 2, sb.toString)
        }
      } else {
        Seq.empty
      }
    }).flatten
  }

  def render(ant: Ant) = {
    println(s"Rendering $ant")
    val farm = new AntFarmImage(width, height)
    farm.add(ant)
    farm.run(stepGenerations)
    if (farm.fillRatio < minFill) {
      ""
    } else {
      var totalGenerations = stepGenerations.toLong
      while(farm.fillRatio < targetFill && totalGenerations < maxGenerations) {
        totalGenerations = totalGenerations + stepGenerations
        farm.run(stepGenerations)
      }
      if(allowDuplicates || dedupSet.add(new AntSignature(farm.histogram))) {
        var imageSrc: String = null
        if (inlineSrc) {
          imageSrc = farm.png()
        } else {
          val imageName = {
            imageIdx += 1;
            imageIdx
          } + ".png"
          farm.png(new File(zooDir, imageName))
          imageSrc = reportName + "/" + imageName
        }
        s"""<img src="$imageSrc" onclick="AntLib.antPopup('${ant.code}')" alt="${ant.toString}" />"""
      } else {
        ""
      }
    }
  }
}
