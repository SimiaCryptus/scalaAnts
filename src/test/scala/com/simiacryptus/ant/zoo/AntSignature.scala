package com.simiacryptus.ant.zoo

import java.util
import java.util.Arrays

class AntSignature(val histogram: Array[Int]) {
  override def hashCode: Int = {
    val prime = 31
    var result = 1
    result = prime * result + util.Arrays.hashCode(histogram)
    result
  }

  override def equals(obj: Any): Boolean = {
    if (this eq obj.asInstanceOf[AnyRef]) return true
    if (obj == null) return false
    if (getClass ne obj.getClass) return false
    val other = obj.asInstanceOf[AntSignature]
    if (!Arrays.equals(histogram, other.histogram)) return false
    true
  }
}
