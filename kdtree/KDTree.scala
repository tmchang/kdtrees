package kdtrees.kdtree
import scala.math
import scala.collection.mutable.ListBuffer 

class KDTree[A <: KDData[A]](data: List[A]) extends KDSearch[A] {
  
  val root: KDTreeNode[A] = new KDTreeNode(findMedian(0, data))
  
  buildTree(0, root, data)  
 
  def buildTree(dim: Int, parent: KDTreeNode[A], dataRemaining: List[A]) {
    var leftData = new ListBuffer[A]()
    var rightData = new ListBuffer[A]()
    if (dataRemaining.length > 0) {
      for (i <- dataRemaining) {
        if (i != parent.datum) {
          if (i.lessThan(parent.datum)(dim)) leftData.append(i)
          else rightData.append(i)
        }
      }
     val leftList = leftData.toList
     val rightList = rightData.toList
     val nextDim = (dim + 1) % parent.datum.dimensions
     val leftChild = findMedian(nextDim, leftList)
     val rightChild = findMedian(nextDim, rightList)
     parent.left = new KDTreeNode(leftChild)
     parent.right = new KDTreeNode(rightChild)
     buildTree(nextDim, parent.left, leftList)
     buildTree(nextDim, parent.right, rightList)
    }
  }  
    
  override def findNN(target: A): Option[A] = {
    return null
  }
  
  private def findMedian(dim: Int, dataList: List[A]) : A = {
    val comparisonFunction = lessThan(dim)_
    val sortedData = dataList.sortWith(comparisonFunction)
    return sortedData(math.floor(sortedData.length/2).toInt)
  }
  
  private def lessThan(dim: Int)(data1: A, data2: A): Boolean = data1.lessThan(data2)(dim)

    

}