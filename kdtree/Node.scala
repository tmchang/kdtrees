package kdtrees.kdtree

class Node extends KDData[Array[Double]] {
    
    val dimensions = 0
    
    override def distance(other: Array[Double]): Double = {
      0.0
    }
    
    override def compDim(dim: Int, other: Array[Double]): Double = {
      0.0
    }
    
}