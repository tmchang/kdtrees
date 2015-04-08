package kdtrees.kdtree

class BruteForce[A <: KDData[A]](data: List[A]) extends KDSearch[A] {
  override def findNN(target: A): Option[A] = {
    // Find the nearest neighbor by exhaustively searching through
    // the list of data to find the closest one to the target.
    if (data.length > 0)
      Some(data.min(Ordering.by { (a : A) => target.distance(a) }))
    else
      None
  }
}

object BruteForce {
  
  def main(args: Array[String]) {
  }
}