package kdtrees.kdtree
import scala.math
class Node(vec: Array[Double]) extends KDData[Array[Double]] {
    
    val dimensions = vec.length
    
    override def distance(other: Array[Double]): Double = {
      var toReturn: Double = 0.0
      for (i <- 0 to dimensions){
        toReturn += math.pow((vec(i) - other(i)),2)
      }
      toReturn = math.sqrt(toReturn)
      return toReturn
    }
    
    override def compDim(dim: Int, other: Array[Double]): Double = {
      0.0
    }
    
}