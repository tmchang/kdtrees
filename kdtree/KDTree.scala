package kdtrees.kdtree
import scala.math
import scala.collection.mutable.ListBuffer 
import scala.collection.mutable.Stack

class KDTree[A <: KDData[A]](data: List[A]) extends KDSearch[A] {
  
  val root: KDTreeNode[A] = new KDTreeNode(findMedian(0, data))
  
  buildTree(0, root, data)  
 
  def buildTree(dim: Int, parent: KDTreeNode[A], dataRemaining: List[A]) {
    var leftData = new ListBuffer[A]()
    var rightData = new ListBuffer[A]()
    if (dataRemaining.length > 0) {
      for (i <- dataRemaining) {
        if (i ne parent.datum) {
          if (i.lessThan(parent.datum)(dim)) leftData.append(i)
          else rightData.append(i)
        }
      }
     val leftList = leftData.toList
     val rightList = rightData.toList
     val nextDim = (dim + 1) % parent.datum.dimensions
     val leftChild = findMedian(nextDim, leftList)
     val rightChild = findMedian(nextDim, rightList)
     parent.left = Some(new KDTreeNode(leftChild))
     parent.right = Some(new KDTreeNode(rightChild))
     buildTree(nextDim, parent.left.get, leftList)
     buildTree(nextDim, parent.right.get, rightList)
    }
  }  
    
  override def findNN(target: A): Option[A] = {
    if (data.length == 0) {
      None
    } else {
      val visitedNodes = new Stack[KDTreeNode[A]]()
      searchTree(root, target, visitedNodes, 0)
      return unwindRecursion(visitedNodes, target, 1000000.0, root.datum, 0)
    }
  }
  
  private def searchTree(
      parent: KDTreeNode[A], 
      target: A, 
      visitedNodes: Stack[KDTreeNode[A]], 
      dim: Int) {
    visitedNodes.push(parent)
    if (target.lessThan(parent.datum)(dim)) {
        parent.left match {
          case None => 
          case Some(newNode) => searchTree(
              newNode,
              target,
              visitedNodes,
              (dim + 1) % parent.datum.dimensions)
        }
      } else {
        parent.right match {
          case None => 
          case Some(newNode) => searchTree(
              newNode,
              target,
              visitedNodes,
              (dim + 1) % parent.datum.dimensions)
        }
      }
  }
   
  private def unwindRecursion(
      visitedNodes: Stack[KDTreeNode[A]], 
      target: A,
      bestDist: Double,
      bestMatch: A,
      baseLevel: Int): Option[A] = {
    if (visitedNodes.length == 0) return Option(bestMatch)
    else {
      val toCheck = visitedNodes.pop
      var newBestDist: Option[Double] =  None
      var newBestMatch: Option[A] = None
      if (toCheck.datum.distance(target) < bestDist) {
        newBestDist = Some(toCheck.datum.distance(target))
        newBestMatch = Some(toCheck.datum)
      } else {
        newBestDist = Some(bestDist)
        newBestMatch = Some(bestMatch)
      }
      if (visitedNodes
          .top
          .datum
          .compDim((visitedNodes.length + baseLevel) % data.length, target) < 
          newBestDist.get) {// There could be nearer points in other branch
        val otherNodes: Stack[KDTreeNode[A]] = new Stack[KDTreeNode[A]]()
        if (visitedNodes.top.left eq Some(toCheck)) {//Already checked the left
          visitedNodes.top.right match {
            case None => 
            case Some(node) => {
              searchTree(//Builds the other stack
                  node, 
                  target, 
                  otherNodes, 
                  (baseLevel + visitedNodes.length + 1) % data.length)
              newBestMatch = unwindRecursion(//Searches the other stack
                  otherNodes, 
                  target, 
                  newBestDist.get, 
                  newBestMatch.get, 
                  (baseLevel + visitedNodes.length + 1) % data.length)
              newBestDist = Some(newBestMatch.get.distance(target))
              return unwindRecursion(// Continue checking the original stack
                  visitedNodes,
                  target,
                  newBestDist.get,
                  newBestMatch.get,
                  baseLevel)
            }
          }
        } else {//already checked the right
          visitedNodes.top.left match {
            case None => 
            case Some(node) => {
              searchTree(//Builds the other stack
                  node, 
                  target, 
                  otherNodes, 
                  (baseLevel + visitedNodes.length + 1) % data.length)
              newBestMatch = unwindRecursion(//Searches the other stack
                  otherNodes, 
                  target, 
                  newBestDist.get, 
                  newBestMatch.get, 
                  (baseLevel + visitedNodes.length + 1) % data.length)
              newBestDist = Some(newBestMatch.get.distance(target))
              return unwindRecursion(// Continue checking the original stack
                  visitedNodes,
                  target,
                  newBestDist.get,
                  newBestMatch.get,
                  baseLevel)
            }
          }
        }
      } 
      //If there couldn't be closer points in other branch
      return unwindRecursion(
                visitedNodes,
                target, 
                newBestDist.get, 
                newBestMatch.get,
                baseLevel)
    }
  }
  
  private def findMedian(dim: Int, dataList: List[A]) : A = {
    val comparisonFunction = lessThan(dim)_
    val sortedData = dataList.sortWith(comparisonFunction)
    return sortedData(math.floor(sortedData.length/2).toInt)
  }
  
  private def lessThan(dim: Int)(data1: A, data2: A): Boolean = data1.lessThan(data2)(dim)

    

}