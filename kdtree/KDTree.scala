package kdtrees.kdtree
import scala.math
import scala.collection.mutable.ListBuffer 
import scala.collection.mutable.Stack

/**
 * Represents a KDTree paramaterized by A which is a subtype of KDData[A]
 * @constructor - creates a new tree with the data in data
 * @param data - the data with which to build the tree
 */
class KDTree[A <: KDData[A]](data: List[A]) extends KDSearch[A] {
  
  // The root node of the tree
  private val root: KDTreeNode[A] = new KDTreeNode(findMedian(0, data))
  
  // Builds the tree by setting left and right references for all nodes in data
  buildTree(0, root, data)  
 
  /**
   * Recursively builds the tree by modifying the left and right references
   * in the parent node, by filtering all nodes less than the parent 
   * and greater than the parent into the recursion
   * 
   * @param dim - The current dimension to split dataRemaining on
   * @param parent -  The node who's references we are modifying, i.e
   * the median of the split one level above
   * @param dataRemaining - The data that has not been added to the tree yet, and 
   * also satisfies the left/right conditions of the parent node
   */
  def buildTree(dim: Int, parent: KDTreeNode[A], dataRemaining: List[A]) {
    var leftData = new ListBuffer[A]()
    var rightData = new ListBuffer[A]()
    if (dataRemaining.length > 1) {
      for (i <- dataRemaining) {
        if (i ne parent.datum) {
          if (lessThan(dim)(i, parent.datum)) leftData.append(i)
          else rightData.append(i)
        }
      }
     val leftList = leftData.toList
     val rightList = rightData.toList
     val nextDim = (dim + 1) % parent.datum.dimensions
     leftList.length match{
       case 0 => 
       case n => {
         val leftChild = findMedian(nextDim, leftList)
         parent.left = Some(new KDTreeNode(leftChild))
         buildTree(nextDim, parent.left.get, leftList)
       }
     }
     rightList.length match{
       case 0 => 
       case n => {
         val rightChild = findMedian(nextDim, rightList)
         parent.right = Some(new KDTreeNode(rightChild))
         buildTree(nextDim, parent.right.get, rightList)
       }
     }
    }
  }  
    
  override def findNN(target: A): Option[A] = {
    if (data.length == 0) {
      None
    } else {
      val visitedNodes = new Stack[KDTreeNode[A]]()
      searchTree(root, target, visitedNodes, 0) // modifies visitedNodes in place
      // return the min distance to target by unwinding the stack
      // Uses 10000000 as an initial distance, and root as an initial "best" node
      return unwindRecursion(visitedNodes, target, 1000000.0, root.datum, 0)
    }
  }
  
  /**
   * Performs KD search (binary search by dimension) to find target
   * beginning at target. Stores each node visited in Stack visitedNodes
   * @param parent - the node to start the search from
   * @param target - the data searched for
   * @param visitedNodes - A stack to add visited nodes to, modified in place
   * @param dim - the dimension to split on 
   */
  private def searchTree(
      parent: KDTreeNode[A], 
      target: A, 
      visitedNodes: Stack[KDTreeNode[A]], 
      dim: Int) {
    
    visitedNodes.push(parent) // parent is visited
    if (lessThan(dim)(target, parent.datum)) {// Search left
        parent.left match {
          case None => // Leaf Node
          case Some(newNode) => searchTree(
              newNode,
              target,
              visitedNodes,
              (dim + 1) % parent.datum.dimensions)
        }
      } else {// Search right
        parent.right match {
          case None => // Leaf Node
          case Some(newNode) => searchTree(
              newNode,
              target,
              visitedNodes,
              (dim + 1) % parent.datum.dimensions)
        }
      }
  }
  
  /**
   * Recursively pops each item off of a Stack of visited nodes, computing
   * the best distance at each node, and also checking if a smaller distance
   * could be found on the other side of the next node. If this is true 
   * searchTree will be called to build a Stack of the other branch, which will
   * then be fed into a unwindRecursion call
   * @param visitedNodes - a Stack of nodes visited by searchTree
   * @param target -  the target data to compute the distance from
   * @param bestDist -  the best distance found so far
   * @param bestMatch - the data with the best distance so far
   * @param baseLevel - an indentifier of which dimension the node in the bottom
   * of the Stack was split on
   * @return - Optionally the nearest datum to target
   */
  private def unwindRecursion(
      visitedNodes: Stack[KDTreeNode[A]], 
      target: A,
      bestDist: Double,
      bestMatch: A,
      baseLevel: Int): Option[A] = {
    
    val toCheck = visitedNodes.pop // The node we are checking for distance
    val dim = toCheck.datum.dimensions
    var newBestDist: Option[Double] =  None
    var newBestMatch: Option[A] = None
    if (toCheck.datum.distance(target) < bestDist) {
      newBestDist = Some(toCheck.datum.distance(target))
      newBestMatch = Some(toCheck.datum)
    } else {
      newBestDist = Some(bestDist)
      newBestMatch = Some(bestMatch)
    }
    
    //print here if other branch is checked
    if (visitedNodes.length == 0) return newBestMatch //End recursion
    if (math.abs(visitedNodes
        .top
        .datum
        .compDim((visitedNodes.length - 1 + baseLevel) % dim, target)) <
        newBestDist.get) {// There could be nearer points in other branch
      val otherNodes: Stack[KDTreeNode[A]] = new Stack[KDTreeNode[A]]()
      if (visitedNodes.top.left.get eq toCheck) {//Already checked the left
        visitedNodes.top.right match {
          case None => 
          case Some(node) => {
            searchTree(//Builds the other stack
                node, 
                target, 
                otherNodes, 
                (baseLevel + visitedNodes.length) % dim)
            newBestMatch = unwindRecursion(//Searches the other stack
                otherNodes, 
                target, 
                newBestDist.get, 
                newBestMatch.get, 
                (baseLevel + visitedNodes.length) % dim)
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
                (baseLevel + visitedNodes.length) % dim)
            newBestMatch = unwindRecursion(//Searches the other stack
                otherNodes, 
                target, 
                newBestDist.get, 
                newBestMatch.get, 
                (baseLevel + visitedNodes.length) % dim)
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
  
  /**
   * finds the Median in a list of KDData by sorting based on 
   * a certain dimension, uses private lessThan as a helper
   * @param dim - the dimension with which to sort
   * @param dataList - the list of KDData to sort
   * @return - the median KDData
   */
  private def findMedian(dim: Int, dataList: List[A]) : A = {
    val comparisonFunction = lessThan(dim)_
    val sortedData = dataList.sortWith(comparisonFunction)
    return sortedData(math.floor(sortedData.length/2).toInt)
  }
  
  /**
   * Returns true if data1 is less than data2 in the specific dimension,
   * a helper function for findMedian, this provides a curryable comparison
   * function for two KDData types by using the builtin KDData.lessThan function
   * @param dim - the dimension to compare on
   * @param data1 - the first data
   * @param data2 - the second data
   * @return - true if data1 < data2 in the dimension dim
   */
  private def lessThan(dim: Int)(data1: A, data2: A): Boolean = 
    (data1.compDim(dim, data2) < 0)
  
  /**
   * Returns an array created from the KDTree, recursively putting all nodes
   * to the left of a parent node earlier in an array
   * Used in toString and in testing
   * @return - an array containing all the data in KDTree, in left most fasion
   */
  def toArray(): Array[Option[KDTreeNode[A]]] = {
    
    /**
     * toArray is the wrapper for this function
     */
    def helper(
        level: Int, 
        node: Option[KDTreeNode[A]]): Array[Option[KDTreeNode[A]]] = {
      if (level >=2) Array(node)
      else node match {
        case None => Array(None)
        case Some(d) => Array(
            helper(level + 1, node.get.left), 
            Array(node), 
            helper(level + 1, node.get.right)).flatten
      }
    }
    helper(0, Some(root))
  }
  
  override def toString(): String = {
    
    val sb = new StringBuilder()
    for (o <- toArray()) {
      o match {
        case None => sb.append("Leaf")
        case Some(d) => sb.append(d.datum.toString())
      }
    }
    sb.toString
  }
  
  /**
   * Returns the size of the tree, used for testing balance
   * (testing if the left branch and right branch are of similar size)
   * @param n - the root node to compute the size of
   * @return - and int of the size of the tree
   */
  def size(n: Option[KDTreeNode[A]]): Int = {
    n match {
      case None => 0
      case Some(d) => (1 + size(d.left) + size(d.right))
    }
  }
    

}

object KDTree {
  
  
  def main(args: Array[String]) {
    val p1 = new Profile(Array(10.0, 10.0), "a")
    val p2 = new Profile(Array(9.0,8.0),"b")
    val p3 = new Profile(Array(9.0,7.0),"c")
    val p4 = new Profile(Array(4.0,9.0),"d")
    val p5 = new Profile(Array(15.0,9.0),"e")
    val p6 = new Profile(Array(14.0,5.0),"f")
    val p7 = new Profile(Array(12.0,10.0),"g")
    val l1 = List(p1,p2,p3,p4,p5,p6,p7)
    val test = new KDTree[Profile](l1)
    println("Nodes from left to right")
    println(test.toString())
    println(test.findNN(new Profile(Array(13.0, 4.0),"test")))
    
    val p12 = new Profile(Array(4.0, 7.0, 4.0), "a")
    val p22 = new Profile(Array(3.0,4.0,6.0),"b")
    val p32 = new Profile(Array(5.0,5.0,2.0),"c")
    val p42 = new Profile(Array(1.0,2.0,9.0),"d")
    val p52 = new Profile(Array(2.0,8.0,5.0),"e")
    val p62 = new Profile(Array(6.0,1.0,7.0),"f")
    val p72 = new Profile(Array(6.0,9.0,8.0),"g")
    val l12 = List(p12,p22,p32,p42,p52,p62,p72)
    val test2 = new KDTree[Profile](l12)
    println("Nodes from left to right")
    println(test2.toString())
    println(test2.findNN(new Profile(Array(3.0, 9.0, 8.0),"test2")))
    
    //Test loading csv and findNN 
    import kdtrees.gui.Parser
    import scala.util.Random
    val csvProfiles = Parser.csvToProfiles("src/kdtrees/profiles.csv")
    val bigTree = new KDTree(csvProfiles)
    val brute = new BruteForce(csvProfiles)
    val f1 = new Profile(Array(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0), "")
    // Print out the results to see that they are correct
    println(brute.findNN(f1).get)
    println(bigTree.findNN(f1).get)
    // Show that these are equal
    println(brute.findNN(f1).get == bigTree.findNN(f1).get)
    // 20 random test cases to show equivalence between BruteForce
    // and KDTree
    
    
    for (i <- 0 until 5) {
      val a = Array.fill(10)(Random.nextDouble)
      val f = new Profile(a, "")
      println(brute.findNN(f).get == bigTree.findNN(f).get)
    }
    //To show that the tree built from csv is balanced
    println("Size of left branch: ")
    println(bigTree.size(bigTree.root.left))
    println("Size of right branch: ")
    println(bigTree.size(bigTree.root.right))
    
    
    
    
  }
}