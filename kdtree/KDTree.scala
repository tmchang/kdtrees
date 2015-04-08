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
    if (dataRemaining.length > 1) {
      for (i <- dataRemaining) {
        if (i ne parent.datum) {
          if (i.lessThan(parent.datum)(dim)) leftData.append(i)
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
    val toCheck = visitedNodes.pop
    val dim = toCheck.datum.dimensions
    //println("Current Node " + toCheck.datum)
    //println("Nodes in stack:")
    /*
    for (n <- visitedNodes){
      println(n.datum)
    }*/
    var newBestDist: Option[Double] =  None
    var newBestMatch: Option[A] = None
    if (toCheck.datum.distance(target) < bestDist) {
      newBestDist = Some(toCheck.datum.distance(target))
      newBestMatch = Some(toCheck.datum)
    } else {
      newBestDist = Some(bestDist)
      newBestMatch = Some(bestMatch)
    }
    if (visitedNodes.length == 0) return newBestMatch
    //println((visitedNodes.length - 1 + baseLevel) % dim)
    if (visitedNodes
        .top
        .datum
        .compDim((visitedNodes.length - 1 + baseLevel) % dim, target) < 
        newBestDist.get) {// There could be nearer points in other branch
      val otherNodes: Stack[KDTreeNode[A]] = new Stack[KDTreeNode[A]]()
      if (visitedNodes.top.left.get eq toCheck) {//Already checked the left
        //println("Already checked left")
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
        //println("Already checked right")
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
  
  private def findMedian(dim: Int, dataList: List[A]) : A = {
    val comparisonFunction = lessThan(dim)_
    val sortedData = dataList.sortWith(comparisonFunction)
    return sortedData(math.floor(sortedData.length/2).toInt)
  }
  
  private def lessThan(dim: Int)(data1: A, data2: A): Boolean = data1.lessThan(data2)(dim)
  
  def toArray(): Array[Option[KDTreeNode[A]]] = {
    
    def helper(level: Int, node: Option[KDTreeNode[A]]): Array[Option[KDTreeNode[A]]] = {
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
    println(brute.findNN(f1).get)
    println(bigTree.findNN(f1).get)
    val f2 = new Profile(Array.fill(10)(Random.nextDouble), "")
    println(brute.findNN(f2).get == bigTree.findNN(f2).get)
    
    
  }
}